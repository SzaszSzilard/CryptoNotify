package com.crypto.notify.scheduler;

import com.crypto.notify.dto.CryptoPriceHistoryModel;
import com.crypto.notify.dto.CryptoPriceModel;
import com.crypto.notify.service.KeyDbService;
import com.crypto.notify.service.NotificationService;
import com.crypto.notify.util.CryptoDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
        keyDbService.getFullList("n_above")
                .map(cryptoDTOMapper::toPriceAbove)
                .flatMap(notification -> notificationService.priceTargetReached(notification)
                        .filter(Boolean::booleanValue)
                        .doOnNext(shouldNotify -> {
                            log.info("Price target reached for notification: {}", notification);
                            notificationService.delete(notification);
                        })
                )
                .subscribe();
    }
}