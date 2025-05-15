package com.crypto.notify.model.notificationTypes;

import com.crypto.notify.model.notificationBase.PriceTargetNotificationModel;

public class PriceAboveModel extends PriceTargetNotificationModel {
    public PriceAboveModel(String userId, String symbol, Double price) {
        super(userId, symbol, price);
        this.type = "n_above";
    }

    public boolean isAboveTargetPrice(Double currentPrice) {
        return currentPrice > this.price;
    }
}
