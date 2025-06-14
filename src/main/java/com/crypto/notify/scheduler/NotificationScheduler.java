package com.crypto.notify.scheduler;

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
                keyDbService.getFullList("n_above:*").map(cryptoDTOMapper::toPriceAbove),
                keyDbService.getFullList("n_below:*").map(cryptoDTOMapper::toPriceBelow),
                keyDbService.getFullList("n_percent_above:*").map(cryptoDTOMapper::toPercentageAbove),
                keyDbService.getFullList("n_percent_below:*").map(cryptoDTOMapper::toPercentageBelow)
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