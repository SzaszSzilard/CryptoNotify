package com.crypto.notify.model.notificationType;

import com.crypto.notify.constants.NotificationTypeConstants;
import com.crypto.notify.model.notificationBase.PercentageChangeModel;

import java.util.List;

public class PercentageAboveModel extends PercentageChangeModel {
    public PercentageAboveModel(String userId, String symbol, Double price, Double percentage) {
        super(userId, symbol, price, percentage);
        this.type = NotificationTypeConstants.N_PERCENT_ABOVE;
    }

    @Override
    public double shouldNotify(List<Double> currentPrice) {
        return (currentPrice.getFirst() > this.price * (1 + this.percentage / 100)) ? currentPrice.getFirst() : 0;
    }

    @Override
    public String getNotificationTitle() {
        return String.format("%s Rises!", this.symbol);
    }

    @Override
    public String getNotificationMessage(Object... params) {
        return String.format(
            "The price of %s has risen %.2f%% percent, now its above your target of %.2f.",
            this.symbol, this.percentage, this.price * (1 + this.percentage / 100)
        );
    }
}
