package com.crypto.notify.model.notificationBase;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public abstract class PriceTargetNotificationModel extends NotificationModel {
    protected Double price;

    public PriceTargetNotificationModel(String userId, String symbol, Double price) {
        super(userId, symbol);
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }
}
