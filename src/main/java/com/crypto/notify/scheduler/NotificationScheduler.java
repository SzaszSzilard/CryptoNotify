package com.crypto.notify.scheduler;

import com.crypto.notify.constants.Constants;
import com.crypto.notify.constants.NotificationTypeConstants;
import com.crypto.notify.dto.CryptoHistoryModel;
import com.crypto.notify.dto.CryptoModel;
import com.crypto.notify.service.KeyDbService;
import com.crypto.notify.service.NotificationService;
import com.crypto.notify.service.PushNotificationService;
import com.crypto.notify.util.CryptoDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NotificationScheduler {

    private final KeyDbService keyDbService;
    private final CryptoDTOMapper cryptoDTOMapper;
    private final Logger log = LoggerFactory.getLogger(NotificationScheduler.class);
    private final NotificationService notificationService;


    public NotificationScheduler(KeyDbService keyDbService,
                                 CryptoDTOMapper cryptoDTOMapper,
                                 NotificationService notificationService) {
        this.keyDbService = keyDbService;
        this.cryptoDTOMapper = cryptoDTOMapper;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedRate = 1000*60)
    public void PriceTargetNotificationSender() {
        Flux<CryptoModel> cryptoPrices = keyDbService.getValue(Constants.CRYPTO_PRICES)
                .flatMapMany(cryptoDTOMapper::toCrypto)
                .cache();

        cryptoPrices.thenMany(
            Flux.concat(
                keyDbService.getAllCombinedKeys(NotificationTypeConstants.N_ABOVE + ":*"),
                keyDbService.getAllCombinedKeys(NotificationTypeConstants.N_BELOW + ":*"),
                keyDbService.getAllCombinedKeys(NotificationTypeConstants.N_PERCENT_ABOVE + ":*"),
                keyDbService.getAllCombinedKeys(NotificationTypeConstants.N_PERCENT_BELOW + ":*")
            ).map(cryptoDTOMapper::toPriceTarget).cache()
                .flatMap(notification -> notificationService.priceTargetReached(notification, cryptoPrices)
                        .filter(result -> result != 0)
                        .flatMap(_ -> {
                            PushNotificationService.sendPushNotification(
                                    notification.getNotificationTitle(),
                                    notification.getNotificationMessage(),
                                    notification.getUserId()
                            );
                            return notificationService.delete(notification)
                                    .doOnSuccess(_ -> log.info("Notification deleted, type: {}, id: {}", notification.getType(), notification.getId()));
                        })
                )
        ).subscribe();
    }

    @Scheduled(fixedRate = 1000*60)
    public void NonPriceTargetNotificationSender() {
        LocalDateTime now = LocalDateTime.now();
        String key = "chp" + "-" + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Flux<CryptoHistoryModel> cryptoHistories = keyDbService.getList(key, -12, -1)
                .map(cryptoDTOMapper::toCryptoHistory)
                .cache();

        cryptoHistories.thenMany(
                Flux.concat(
                        keyDbService.getAllCombinedKeys(NotificationTypeConstants.N_RALLY + ":*"),
                        keyDbService.getAllCombinedKeys(NotificationTypeConstants.N_CHANGE + ":*")
                )
                .map(cryptoDTOMapper::toNonTargetNotification)
                .flatMap(notification -> cryptoHistories
                        .flatMap(cryptoHistory -> Flux.fromIterable(cryptoHistory.priceList())
                                .filter(crypto -> crypto.symbol().equals(notification.getSymbol()))
                                .next() // return first matching Crypto
                                .map(CryptoModel::price)
                        )
                        .collectList()
                        .map(notification::shouldNotify)
                        .flatMap(gain -> {
                            if (gain != 0) {
                                PushNotificationService.sendPushNotification(
                                    notification.getNotificationTitle(),
                                    notification.getNotificationMessage(List.of(gain)),
                                    notification.getUserId()
                                );

                                return notificationService.delete(notification)
                                    .doOnSuccess(_ -> log.info("Notification deleted, type: {}, id: {}", notification.getType(), notification.getId()));
                            }
                            return Mono.empty();
                        })
                )
        ).subscribe();
    }
}