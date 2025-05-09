package com.crypto.notify.model;

public class AboveNotificationModel extends PriceTargetNotificationModel {
    private Double targetPrice;

    public AboveNotificationModel(String userId, String symbol, Double targetPrice) {
        super(userId, symbol, targetPrice);
    }

    public boolean isAboveTargetPrice(Double currentPrice) {
        return currentPrice < targetPrice;
    }
}
