package com.crypto.notify.scheduler;

import com.crypto.notify.constants.Constants;
import com.crypto.notify.constants.NotificationTypeConstants;
import com.crypto.notify.dto.CryptoHistoryModel;
import com.crypto.notify.dto.CryptoModel;
import com.crypto.notify.model.notificationBase.NonTargetNotificationModel;
import com.crypto.notify.model.notificationBase.PriceTargetNotificationModel;
import com.crypto.notify.service.KeyDbService;
import com.crypto.notify.service.NotificationService;
import com.crypto.notify.service.PushNotificationService;
import com.crypto.notify.util.CryptoDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@Service
public class NotificationScheduler {

    private final KeyDbService keyDbService;
    private final Logger log = LoggerFactory.getLogger(NotificationScheduler.class);
    private final NotificationService notificationService;

    public NotificationScheduler(KeyDbService keyDbService,
                                 NotificationService notificationService) {
        this.keyDbService = keyDbService;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedRate = 1000 * 60)
    public void PriceTargetNotificationSender() {
        try {
            String rawPrices = keyDbService.getValue(Constants.CRYPTO_PRICES);
            List<CryptoModel> cryptoPrices = CryptoDTOMapper.toCrypto(rawPrices);
            if (cryptoPrices.isEmpty()) {
                return;
            }

            List<PriceTargetNotificationModel> notifications = Stream.of(
                            keyDbService.getAllCombinedKeys(NotificationTypeConstants.N_ABOVE + ":*"),
                            keyDbService.getAllCombinedKeys(NotificationTypeConstants.N_BELOW + ":*"),
                            keyDbService.getAllCombinedKeys(NotificationTypeConstants.N_PERCENT_ABOVE + ":*"),
                            keyDbService.getAllCombinedKeys(NotificationTypeConstants.N_PERCENT_BELOW + ":*")
                    )
                    .flatMap(List::stream)
                    .map(CryptoDTOMapper::toPriceTarget)
                    .toList();

            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (PriceTargetNotificationModel notification : notifications) {
                    executor.submit(() -> {
                        double result = notificationService.priceTargetReached(notification, cryptoPrices);
                        if (result != 0) {
                            PushNotificationService.sendPushNotification(
                                    notification.getNotificationTitle(),
                                    notification.getNotificationMessage(),
                                    notification.getUserId()
                            );
                            notificationService.delete(notification);
                            log.info("Notification deleted, type: {}, id: {}", notification.getType(), notification.getId());
                        }
                    });
                }
            }
        } catch (Exception e) {
            log.warn("Price target notification check skipped/failed: {}", e.getMessage());
        }
    }

    @Scheduled(fixedRate = 1000 * 60)
    public void NonPriceTargetNotificationSender() {
        try {
            LocalDateTime now = LocalDateTime.now();
            String key = "chp" + "-" + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            List<CryptoHistoryModel> cryptoHistories = keyDbService.getList(key, -12, -1)
                    .stream()
                    .map(CryptoDTOMapper::toCryptoHistory)
                    .toList();

            if (cryptoHistories.isEmpty()) {
                return;
            }

            List<NonTargetNotificationModel> notifications = Stream.of(
                            keyDbService.getAllCombinedKeys(NotificationTypeConstants.N_RALLY + ":*"),
                            keyDbService.getAllCombinedKeys(NotificationTypeConstants.N_CHANGE + ":*")
                    )
                    .flatMap(List::stream)
                    .map(CryptoDTOMapper::toNonTargetNotification)
                    .toList();

            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (NonTargetNotificationModel notification : notifications) {
                    executor.submit(() -> {
                        List<Double> prices = cryptoHistories.stream()
                                .map(cryptoHistory -> cryptoHistory.priceList().stream()
                                        .filter(crypto -> crypto.symbol().equals(notification.getSymbol()))
                                        .findFirst()
                                        .map(CryptoModel::price)
                                        .orElse(null))
                                .filter(Objects::nonNull)
                                .toList();

                        double gain = notification.shouldNotify(prices);
                        if (gain != 0) {
                            PushNotificationService.sendPushNotification(
                                    notification.getNotificationTitle(),
                                    notification.getNotificationMessage(gain),
                                    notification.getUserId()
                            );
                            notificationService.delete(notification);
                            log.info("Notification deleted, type: {}, id: {}", notification.getType(), notification.getId());
                        }
                    });
                }
            }
        } catch (Exception e) {
            log.warn("Non-price target notification check skipped/failed: {}", e.getMessage());
        }
    }
}
