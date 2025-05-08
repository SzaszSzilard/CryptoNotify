package com.crypto.notify.model;

import static java.lang.Math.abs;

public class ShortTermRally extends NotificationModel {
    private String time;
    private Double percentageChange;

    public ShortTermRally(String userId, String symbol, String time, Double percentageChange) {
        super(userId, symbol);
        this.time = time;
        this.percentageChange = percentageChange;
    }

    public boolean isRallying(String time, Double percentageChange) {
        // get timeframe from redis form and compare percentage
        Double oldestPrice = 0.0;
        Double currentPrice = 0.0;

        return Math.abs(currentPrice - (oldestPrice * (1 + percentageChange))) >= percentageChange;
    }
}
