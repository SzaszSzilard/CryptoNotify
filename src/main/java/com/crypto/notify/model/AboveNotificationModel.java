package com.crypto.notify.model;

import com.crypto.notify.service.KeyDbService;
import org.springframework.beans.factory.annotation.Autowired;

public class AboveNotificationModel extends PriceTargetNotificationModel {
    @Autowired
    KeyDbService keyDbService;

    private Double targetPrice;

    public AboveNotificationModel(String userId, String symbol, Double targetPrice) {
        super(userId, symbol, targetPrice);
    }

    public boolean isAboveTargetPrice(Double currentPrice) {
        return currentPrice < targetPrice;
    }

    @Override
    public void setAutoId(String notificationType) {
        this.setId(keyDbService.getAutoID("n_above").block());
    }
}
