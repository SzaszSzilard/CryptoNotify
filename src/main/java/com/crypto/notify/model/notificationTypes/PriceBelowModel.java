package com.crypto.notify.model.notificationTypes;

import com.crypto.notify.model.notificationBase.PriceTargetNotificationModel;
import reactor.core.publisher.Mono;

public class PriceBelowModel extends PriceTargetNotificationModel {
    public PriceBelowModel(String userId, String symbol, Double targetPrice) {
        super(userId, symbol, targetPrice);
    }

    public boolean isBelowTargetPrice(Double currentPrice) {
        return currentPrice < this.price;
    }

    @Override
    public Mono<Long> save() {
        return this.notificationService.saveNotification("n_below", this);
    }
}
