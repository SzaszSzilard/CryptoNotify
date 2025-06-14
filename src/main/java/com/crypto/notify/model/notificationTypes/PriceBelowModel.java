package com.crypto.notify.model.notificationTypes;

import com.crypto.notify.model.notificationBase.PriceTargetNotificationModel;

public class PriceBelowModel extends PriceTargetNotificationModel {
    public PriceBelowModel(String userId, String symbol, Double price) {
        super(userId, symbol, price);
        this.type = "n-below";
     }

    public boolean shouldNotify(Double currentPrice) {
        return currentPrice < this.price;
    }
}
