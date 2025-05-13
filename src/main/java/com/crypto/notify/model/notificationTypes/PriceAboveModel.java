package com.crypto.notify.model.notificationTypes;

import com.crypto.notify.model.notificationBase.PriceTargetNotificationModel;
import reactor.core.publisher.Mono;

public class PriceAboveModel extends PriceTargetNotificationModel {
    public PriceAboveModel(String userId, String symbol, Double price) {
        super(userId, symbol, price);
    }

    public boolean isAboveTargetPrice(Double currentPrice) {
        return currentPrice > this.price;
    }

    @Override
    public Mono<Long> save() {
        return this.notificationService.saveNotification("n_above", this);
    }
}
