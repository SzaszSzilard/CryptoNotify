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

    public String getNotificationTitle() {
        return String.format("%s Falls!", this.symbol);
    }

    public String getNotificationMessage() {
        return String.format(
            "The price of %s has fallen %.2f%% percent, now its below your target of %.2f%%",
            this.symbol, this.percentage, this.price * (1 - this.percentage / 100)
        );
    }
}
