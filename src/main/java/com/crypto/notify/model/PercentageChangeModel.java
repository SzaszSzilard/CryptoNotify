package com.crypto.notify.model;

public class PercentageChangeModel {
    private Double percentageChange;

    public PercentageChangeModel(String userId, String symbol, Double targetPrice, Double percentageChange) {
        this.percentageChange = percentageChange;
    }

    public Double getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(Double percentageChange) {
        this.percentageChange = percentageChange;
    }
}
