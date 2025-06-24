package com.crypto.notify.model.notificationTypes;

import com.crypto.notify.model.notificationBase.PercentageChangeModel;

public class PercentageAboveModel extends PercentageChangeModel {
    public PercentageAboveModel(String userId, String symbol, Double price, Double percentage) {
        super(userId, symbol, price, percentage);
        this.type = "n-percent-above";
    }

    public boolean shouldNotify(Double currentPrice) {
        return currentPrice > this.price * (1 + this.percentage / 100);
    }

    public String getNotificationTitle() {
        return String.format("%s Rises!", this.symbol);
    }

    public String getNotificationMessage() {
        return String.format(
            "The price of %s has risen %.2f%% percent, now its above your targer of %.2f%%",
            this.symbol, this.percentage, this.price * (1 + this.percentage / 100)
        );
    }
}
