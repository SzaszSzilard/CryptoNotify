package com.crypto.notify.model.notificationType;

import com.crypto.notify.constants.NotificationTypeConstants;
import com.crypto.notify.model.notificationBase.NonTargetNotificationModel;

import java.util.List;

public class DirectionChangeModel extends NonTargetNotificationModel {

    public DirectionChangeModel(String userId, String symbol, String time) {
        super(userId, symbol, time);
        this.type = NotificationTypeConstants.N_DIRECTION_CHANGE;
    }

    @Override
    public double shouldNotify(List<Double> prices) {
//        previousWindow = [102, 101, 100]
//        currentWindow = [101, 103, 105]
//        prevTrend = getTrend(previous_window)
//        currTrend = getTrend(current_window)
//
//        return prev_trend != curr_trend && curr_trend != "flat";

        return 0;
    }
}
