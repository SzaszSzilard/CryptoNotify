package com.crypto.notify.model;

public class PercentageChangeModel extends AboveBelowNotificationModel {
    private Double percentageChange;

    public PercentageChangeModel(String userId, String symbol, Double targetPrice, Double percentageChange) {
        super(userId, symbol, targetPrice);
        this.percentageChange = percentageChange;
    }

    public Double getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(Double percentageChange) {
        this.percentageChange = percentageChange;
    }
}
