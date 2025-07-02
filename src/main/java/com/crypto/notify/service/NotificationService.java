package com.crypto.notify.service;

import com.crypto.notify.dto.CryptoModel;
import com.crypto.notify.model.notificationBase.NotificationModel;
import com.crypto.notify.model.notificationBase.PriceTargetNotificationModel;
import com.crypto.notify.constants.Constants;
import com.crypto.notify.util.CryptoDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NotificationService {
    private final KeyDbService keyDbService;
    private final CryptoDTOMapper cryptoDTOMapper;
    private final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final String ID_COUNTER = "idc:";

    public NotificationService(KeyDbService keyDbService,
                               CryptoDTOMapper cryptoDTOMapper) {
        this.keyDbService = keyDbService;
        this.cryptoDTOMapper = cryptoDTOMapper;
    }

    public Mono<Boolean> priceTargetReached(PriceTargetNotificationModel notification, Flux<CryptoModel> cryptoPrices) {
        return cryptoPrices
                .filter(cryptoPrice -> cryptoPrice.symbol().equals(notification.getSymbol()))
                .next() // return first matching Crypto
                .map(cryptoPrice -> notification.shouldNotify(cryptoPrice.price()));
    }

    public Mono<Long> save(NotificationModel notification) {
        String key = notification.getType() + ":" + notification.getUserId();
        return keyDbService.getIncId(ID_COUNTER + key).flatMap(id -> {
            notification.setId(id);
            return keyDbService.pushIntoList(key, cryptoDTOMapper.toJson(notification));
        });
    }

    public Mono<Long> delete(NotificationModel notification) {
        String key = notification.getType() + ":" + notification.getUserId();
        String json = cryptoDTOMapper.toJson(notification);
        return keyDbService.removeFromList(key, json);
    }
}
