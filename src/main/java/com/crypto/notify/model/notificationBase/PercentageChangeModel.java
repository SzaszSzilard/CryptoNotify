package com.crypto.notify.model.notificationBase;

public abstract class PercentageChangeModel extends PriceTargetNotificationModel {
    protected Double percentageChange;

    public PercentageChangeModel(String userId, String symbol, Double price, Double percentageChange) {
        super(userId, symbol, price);
        this.percentageChange = percentageChange;
    }

    public Double getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(Double percentageChange) {
        this.percentageChange = percentageChange;
    }
}
