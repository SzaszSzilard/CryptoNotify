package com.crypto.notify.model.notificationTypes;

import com.crypto.notify.model.notificationBase.PercentageChangeModel;

public class PercentageAboveModel extends PercentageChangeModel {
    public PercentageAboveModel(String userId, String symbol, Double price, Double percentageChange) {
        super(userId, symbol, price, percentageChange);
        this.type = "n_percent_above";
    }

    public boolean shouldNotify() {
//        return currentPrice > this.price * (1 + this.percentageChange / 100);
        return false;
    }
}
