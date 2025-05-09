package com.crypto.notify.model;

public class BelowNotificationModel extends PriceTargetNotificationModel {
    private Double targetPrice;

    public BelowNotificationModel(String userId, String symbol, Double targetPrice) {
        super(userId, symbol, targetPrice);
    }

    public boolean isBelowTargetPrice(Double currentPrice) {
        return currentPrice > targetPrice;
    }
}
