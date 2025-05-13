package com.crypto.notify.model.notificationTypes;

import com.crypto.notify.model.notificationBase.NotificationModel;
import com.crypto.notify.service.KeyDbService;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import static java.lang.Math.abs;

public class ShortTermRally extends NotificationModel {
    private String time;
    private Double percentageChange;

    @Autowired
    KeyDbService keyDbService;


    public ShortTermRally(String userId, String symbol, String time, Double percentageChange) {
        super(userId, symbol);
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
    public Mono<Long> save() {
        return this.notificationService.saveNotification("n_rally", this);
    }
}
