package com.crypto.notify.model.notificationBase;

public abstract class PercentageChangeModel extends PriceTargetNotificationModel {
    protected Double percentage;

    public PercentageChangeModel(String userId, String symbol, Double price, Double percentage) {
        super(userId, symbol, price);
        this.percentage = percentage;
    }

    public Double getPercentage() {
        return percentage;
    }
}
