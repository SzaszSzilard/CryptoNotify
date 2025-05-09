package com.crypto.notify.model;

public class PriceTargetNotificationModel extends NotificationModel implements PriceTargetNotificationInterface {
    private Double targetPrice;

    public PriceTargetNotificationModel(String userId, String symbol, Double targetPrice) {
        super(userId, symbol);
        this.targetPrice = targetPrice;
    }

    @Override
    public void setTargetPrice(Double targetPrice) {
        this.targetPrice = targetPrice;
    }

    @Override
    public Double getTargetPrice() {
        return targetPrice;
    }
}
