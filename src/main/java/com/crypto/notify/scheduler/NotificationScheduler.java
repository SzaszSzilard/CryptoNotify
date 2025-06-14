package com.crypto.notify.scheduler;

import com.crypto.notify.constants.NotificationTypeConstants;
import com.crypto.notify.service.KeyDbService;
import com.crypto.notify.service.NotificationService;
import com.crypto.notify.util.CryptoDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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
        Flux.concat(
                keyDbService.getAllCombinedKeys(NotificationTypeConstants.N_ABOVE + ":*").map(cryptoDTOMapper::toPriceAbove),
                keyDbService.getAllCombinedKeys(NotificationTypeConstants.N_BELOW + ":*").map(cryptoDTOMapper::toPriceBelow),
                keyDbService.getAllCombinedKeys(NotificationTypeConstants.N_PERCENT_ABOVE + ":*").map(cryptoDTOMapper::toPercentageAbove),
                keyDbService.getAllCombinedKeys(NotificationTypeConstants.N_PERCENT_BELOW + ":*").map(cryptoDTOMapper::toPercentageBelow)
        )
                .flatMap(notification -> notificationService.priceTargetReached(notification)
                        .filter(Boolean::booleanValue)
                        .flatMap(_ -> {
                            log.info("Price target reached for notification, type: {}, id: {}", notification.getType(), notification.getId());
                            // Implement push notification
                            return notificationService.delete(notification)
                                    .doOnSuccess(s -> log.info("Notification deleted, type: {}, id: {}", notification.getType(), notification.getId()));
                        })
                )
                .subscribe();
    }
}