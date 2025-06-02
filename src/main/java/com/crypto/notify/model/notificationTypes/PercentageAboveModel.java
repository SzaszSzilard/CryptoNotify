package com.crypto.notify.model.notificationTypes;

import com.crypto.notify.model.notificationBase.PercentageChangeModel;

public class PercentageAboveModel extends PercentageChangeModel {
    public PercentageAboveModel(String userId, String symbol, Double price, Double percentage) {
        super(userId, symbol, price, percentage);
        this.type = "n_percent_above";
    }

    public boolean shouldNotify(Double currentPrice) {
        return currentPrice > this.price * (1 + this.percentage / 100);
    }
}
