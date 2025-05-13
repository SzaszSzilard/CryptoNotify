package com.crypto.notify.model.notificationTypes;

import com.crypto.notify.model.notificationBase.NotificationModel;
import reactor.core.publisher.Mono;

import static java.lang.Math.abs;

public class DirectionChangeNotification extends NotificationModel {
    public DirectionChangeNotification(String userId, String symbol, String time) {
        super(userId, symbol);
    }

    public boolean directionChanged(String time, Double percentageChange) {
//        previousWindow = [102, 101, 100]
//        currentWindow = [101, 103, 105]
//        prevTrend = getTrend(previous_window)
//        currTrend = getTrend(current_window)

//        return prev_trend != curr_trend && curr_trend != "flat";
        return false;
    }

    @Override
    public Mono<Long> save() {
        return this.notificationService.saveNotification("n_change", this);
    }
}
