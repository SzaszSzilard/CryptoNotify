package com.crypto.notify.model.notificationType;

import com.crypto.notify.constants.NotificationTypeConstants;
import com.crypto.notify.model.notificationBase.PriceTargetNotificationModel;

import java.util.List;

public class PriceAboveModel extends PriceTargetNotificationModel {
    public PriceAboveModel(String userId, String symbol, Double price) {
        super(userId, symbol, price);
        this.type = NotificationTypeConstants.N_ABOVE;
    }

    @Override
    public double shouldNotify(List<Double> currentPrice) {
        return (currentPrice.getFirst() > this.price) ? currentPrice.getFirst() : 0;
    }

    @Override
    public String getNotificationTitle() {
        return String.format("%s Rises!", this.symbol);
    }

    @Override
    public String getNotificationMessage(Object... params) {
        return String.format("The price of %s has risen above your target of %.2f.", this.symbol, this.price);
    }
}
