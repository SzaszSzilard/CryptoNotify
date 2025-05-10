package com.crypto.notify.model;

import com.crypto.notify.service.KeyDbService;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.Math.abs;

public class ShortTermRally extends NotificationModel {
    private String time;
    private Double percentageChange;

    @Autowired
    KeyDbService keyDbService;


    public ShortTermRally(String userId, String symbol, String time, Double percentageChange) {
        super(1L, userId, symbol);
        this.time = time;
        this.percentageChange = percentageChange;
    }

    public boolean isRallying(String time, Double percentageChange) {
        // get timeframe from redis form and compare percentage
        Double oldPrice = 0.0;
        Double currentPrice = 0.0;

        return abs((currentPrice - oldPrice) / currentPrice) > percentageChange;
    }

    @Override
    public void setAutoId(String notificationType) {
        this.setId(keyDbService.getAutoID("n_rally").block());
    }
}
