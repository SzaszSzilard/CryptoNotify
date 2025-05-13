package com.crypto.notify.model.notificationTypes;

import com.crypto.notify.model.notificationBase.PercentageChangeModel;
import reactor.core.publisher.Mono;

public class PercentageBelowModel extends PercentageChangeModel {
    public PercentageBelowModel(String userId, String symbol, Double price, Double percentageChange) {
        super(userId, symbol, price, percentageChange);
    }

    @Override
    public Mono<Long> save() {
        return this.notificationService.saveNotification("n_percent_below", this);
    }
}
