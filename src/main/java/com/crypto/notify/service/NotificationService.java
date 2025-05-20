package com.crypto.notify.service;

import com.crypto.notify.model.notificationBase.NotificationModel;
import com.crypto.notify.model.notificationBase.PriceTargetNotificationModel;
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

    public Mono<Boolean> priceTargetReached(PriceTargetNotificationModel notification) {
        return keyDbService.getValue("crypto_prices")
                .map(cryptoDTOMapper::toCryptoPrice)
                .map(cryptoList -> cryptoList.stream()
                        .filter(cryptoPrice -> cryptoPrice.symbol().equals(notification.getSymbol()))
                        .findFirst()
                        .orElse(null))
                .map(cryptoPrice -> notification.shouldNotify(cryptoPrice.price()));
    }

    public Mono<Long> save(NotificationModel notification) {
        return keyDbService.getIncId(notification.getType()+"_idc").flatMap(id -> {
            notification.setId(id);
            return keyDbService.pushIntoList(notification.getType(), cryptoDTOMapper.toJson(notification));
        });
    }
}
