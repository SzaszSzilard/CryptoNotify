package com.crypto.notify.model.notificationTypes;

import com.crypto.notify.model.notificationBase.PercentageChangeModel;

public class PercentageBelowModel extends PercentageChangeModel {
    public PercentageBelowModel(String userId, String symbol, Double price, Double percentage) {
        super(userId, symbol, price, percentage);
        this.type = "n-percent-below";
    }

    public boolean shouldNotify(Double currentPrice) {
        return currentPrice < this.price * (1 - this.percentage / 100);
    }
}
