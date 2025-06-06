package com.crypto.notify.model.notificationBase;

public abstract class PriceTargetNotificationModel extends NotificationModel {
    protected Double price;

    public PriceTargetNotificationModel(String userId, String symbol, Double price) {
        super(userId, symbol);
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public abstract boolean shouldNotify(Double price);
}
