package com.crypto.notify.model.notificationType;

import com.crypto.notify.constants.NotificationTypeConstants;
import com.crypto.notify.model.notificationBase.NonTargetNotificationModel;

import java.util.List;
import java.util.stream.IntStream;

public class RallyModel extends NonTargetNotificationModel {

    public RallyModel(String userId, String symbol, String time) {
        super(userId, symbol, time);
        this.type = NotificationTypeConstants.N_RALLY;
    }


    public double shouldNotify(List<Double> prices) {
        double threshold = 2.0;
        double gain = (prices.getLast() - prices.getFirst()) / prices.getFirst() * 100;

        long risingSteps = IntStream.range(1, prices.size())
                .filter(i -> prices.get(i) > prices.get(i - 1))
                .count();

        boolean strongEnough = gain > threshold || gain < -threshold;
        boolean consistent = risingSteps >= prices.size() * 0.75;
        boolean isRally = consistent && strongEnough;

        return isRally ? gain : 0 ;
    }


}
