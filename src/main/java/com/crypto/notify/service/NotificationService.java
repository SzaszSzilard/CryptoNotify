package com.crypto.notify.service;

import com.crypto.notify.model.notificationBase.NotificationModel;
import com.crypto.notify.util.CryptoDTOMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NotificationService {
    private final KeyDbService keyDbService;
    private final CryptoDTOMapper cryptoDTOMapper;

    public NotificationService(KeyDbService keyDbService,
                               CryptoDTOMapper cryptoDTOMapper) {
        this.keyDbService = keyDbService;
        this.cryptoDTOMapper = cryptoDTOMapper;
    }

//    public Mono<Boolean> isNotificationAbove(AboveNotificationModel aboveNotification) {
//        return keyDbService.getValue("crypto_prices")
//                .map(cryptoDTOMapper::toCryptoPrice)
//                .map( pricesM -> pricesM.map(prices => prices.stream()
//                        .filter(priceModel -> priceModel.symbol().equals(aboveNotification.getSymbol()))
//                        .toList()
//                        .getFirst()
//                        .price() > aboveNotification.getTargetPrice());
//    }

    public Mono<Long> save(NotificationModel notification) {
        return keyDbService.getIncId(notification.getType()).flatMap(id -> {
            notification.setId(id);
            return keyDbService.pushIntoList(notification.getType(), cryptoDTOMapper.toJson(notification));
        });
    }
}
