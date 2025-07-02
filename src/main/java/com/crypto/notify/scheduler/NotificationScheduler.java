package com.crypto.notify.scheduler;

import com.crypto.notify.constants.Constants;
import com.crypto.notify.constants.NotificationTypeConstants;
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
import java.util.Optional;

@Service
public class NotificationScheduler {

    private final KeyDbService keyDbService;
    private final CryptoDTOMapper cryptoDTOMapper;
    private final Logger log = LoggerFactory.getLogger(NotificationScheduler.class);
    private final NotificationService notificationService;
    private final PushNotificationService pushNotificationService;


    public NotificationScheduler(KeyDbService keyDbService,
                                 CryptoDTOMapper cryptoDTOMapper,
                                 NotificationService notificationService,
                                 PushNotificationService pushNotificationService) {
        this.keyDbService = keyDbService;
        this.cryptoDTOMapper = cryptoDTOMapper;
        this.notificationService = notificationService;
        this.pushNotificationService = pushNotificationService;
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
            ).map(cryptoDTOMapper::toPriceTarget)
                .flatMap(notification -> notificationService.priceTargetReached(notification, cryptoPrices)
                        .filter(Boolean::booleanValue)
                        .flatMap(_ -> {
                            PushNotificationService.sendPushNotification(
                                    notification.getNotificationTitle(),
                                    notification.getNotificationMessage(),
                                    notification.getUserId()
                            );
                            return notificationService.delete(notification)
                                    .doOnSuccess(s -> log.info("Notification deleted, type: {}, id: {}", notification.getType(), notification.getId()));
                        })
                )
        ).subscribe();
    }

    @Scheduled(fixedRate = 1000*60)
    public void PriceTrendReversalNotificationSender() {
        LocalDateTime now = LocalDateTime.now();
        String key = "chp" + "-" + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        keyDbService.getList(key, -12, -1)
                .map(cryptoDTOMapper::toCryptoHistory)
                .collectList()
                .flatMapMany(cryptoHistories ->
                        keyDbService.getAllCombinedKeys(NotificationTypeConstants.N_RALLY + ":*")
                                .map(cryptoDTOMapper::toRally)
                                .flatMap(notification_rally -> {
                                    List<Double> last12prices = cryptoHistories.stream()
                                            .map(cryptoHistory -> cryptoHistory.priceList().stream()
                                                    .filter(crypto -> crypto.symbol().equals(notification_rally.getSymbol()))
                                                    .findFirst()
                                                    .map(CryptoModel::price)
                                            )
                                            .flatMap(Optional::stream)
                                            .toList();

                                    double gain = notification_rally.shouldNotify(last12prices);
                                    if (gain != 0) {
                                        PushNotificationService.sendPushNotification(
                                                "Price Trend Reversal Detected",
                                                "A price trend reversal has been detected for " + notification_rally.getSymbol(),
                                                notification_rally.getUserId()
                                        );
                                        return notificationService.delete(notification_rally)
                                                .doOnSuccess(s -> log.info("Price trend reversal notification deleted, symbol: {}, id: {}", notification_rally.getSymbol(), notification_rally.getId()));
                                    }
                                    return Mono.empty();
                                })
                )
                .subscribe();
    }
}