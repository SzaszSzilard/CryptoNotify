package com.crypto.notify.model.notificationTypes;

import com.crypto.notify.model.notificationBase.PriceTargetNotificationModel;
import com.crypto.notify.service.KeyDbService;
import com.crypto.notify.util.CryptoDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

public class PriceBelowModel extends PriceTargetNotificationModel {
//    @Autowired
//    private final KeyDbService keyDbService;
//    @Autowired
//    private final CryptoDTOMapper cryptoDTOMapper;

    public PriceBelowModel(String userId, String symbol, Double price) {
        super(userId, symbol, price);
        this.type = "n_below";
     }

    public Mono<Boolean> shouldNotify() {
//        return keyDbService.getValue("crypto_prices")
//                .map(cryptoDTOMapper::toCryptoPrice)
//                .map(cryptoList -> cryptoList.stream()
//                        .filter(cryptoPrice -> cryptoPrice.symbol().equals(this.symbol))
//                        .findFirst()
//                        .orElse(null))
//                .map(cryptoPrice -> cryptoPrice.price() > this.price);

        return Mono.just(false);
    }
}
