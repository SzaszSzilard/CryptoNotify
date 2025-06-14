package com.crypto.notify.model.notificationTypes;

import com.crypto.notify.model.notificationBase.NotificationModel;
import reactor.core.publisher.Mono;

import static java.lang.Math.abs;

public class ShortTermRally extends NotificationModel {
    private String time;
    private Double percentageChange;

    public ShortTermRally(String userId, String symbol, String time, Double percentageChange) {
        super(userId, symbol);
        this.time = time;
        this.percentageChange = percentageChange;
        this.type = "n-rally";
    }

    public Mono<Boolean> shouldNotify() {
        // get timeframe from redis form and compare percentage
//        Double oldPrice = 0.0;
//        Double currentPrice = 0.0;
//
//        return abs((currentPrice - oldPrice) / currentPrice) > percentageChange;

        return Mono.just(false);
    }
}
