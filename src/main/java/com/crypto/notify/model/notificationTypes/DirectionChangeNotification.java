package com.crypto.notify.model.notificationTypes;

import com.crypto.notify.model.notificationBase.NotificationModel;
import reactor.core.publisher.Mono;

public class DirectionChangeNotification extends NotificationModel {
    private final String time;

    public DirectionChangeNotification(String userId, String symbol, String time) {
        super(userId, symbol);
        this.time = time;
        this.type = "n_change";
    }

    public Mono<Boolean> shouldNotify() {
//        previousWindow = [102, 101, 100]
//        currentWindow = [101, 103, 105]
//        prevTrend = getTrend(previous_window)
//        currTrend = getTrend(current_window)

//        return prev_trend != curr_trend && curr_trend != "flat";
        return Mono.just(false);    }
}
