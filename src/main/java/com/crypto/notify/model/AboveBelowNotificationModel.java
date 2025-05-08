package com.crypto.notify.model;

public class AboveBelowNotificationModel extends NotificationModel implements TargetPriceNotificationInterface {
    private Double targetPrice;

    public AboveBelowNotificationModel(String userId, String symbol, Double targetPrice) {
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

    public boolean isAboveTargetPrice(Double currentPrice) {
        return currentPrice > targetPrice;
    }

    public boolean isBelowTargetPrice(Double currentPrice) {
        return currentPrice < targetPrice;
    }
}
