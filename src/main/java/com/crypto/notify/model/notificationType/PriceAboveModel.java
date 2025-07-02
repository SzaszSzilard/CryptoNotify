package com.crypto.notify.model.notificationType;

import com.crypto.notify.constants.NotificationTypeConstants;
import com.crypto.notify.model.notificationBase.PriceTargetNotificationModel;

public class PriceAboveModel extends PriceTargetNotificationModel {
    public PriceAboveModel(String userId, String symbol, Double price) {
        super(userId, symbol, price);
        this.type = NotificationTypeConstants.N_ABOVE;
    }

    public boolean shouldNotify(Double currentPrice) {
        return currentPrice > this.price;
    }

    public String getNotificationTitle() {
        return String.format("%s Rises!", this.symbol);
    }

    public String getNotificationMessage() {
        return String.format("The price of %s has risen above your target of %.2f!", this.symbol, this.price);
    }
}
