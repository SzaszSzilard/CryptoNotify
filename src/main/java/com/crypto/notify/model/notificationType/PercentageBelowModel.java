package com.crypto.notify.model.notificationType;

import com.crypto.notify.constants.NotificationTypeConstants;
import com.crypto.notify.model.notificationBase.PercentageChangeModel;

import java.util.List;

public class PercentageBelowModel extends PercentageChangeModel {
    public PercentageBelowModel(String userId, String symbol, Double price, Double percentage) {
        super(userId, symbol, price, percentage);
        this.type = NotificationTypeConstants.N_PERCENT_BELOW;
    }

    @Override
    public double shouldNotify(List<Double> currentPrice) {
        return (currentPrice.getFirst() < this.price * (1 - this.percentage / 100)) ? currentPrice.getFirst() : 0;
    }

    @Override
    public String getNotificationTitle() {
        return String.format("%s Falls!", this.symbol);
    }

    @Override
    public String getNotificationMessage(Object... params) {
        return String.format(
            "The price of %s has fallen %.2f%% percent, now its below your target of %.2f.",
            this.symbol, this.percentage, this.price * (1 - this.percentage / 100)
        );
    }
}
