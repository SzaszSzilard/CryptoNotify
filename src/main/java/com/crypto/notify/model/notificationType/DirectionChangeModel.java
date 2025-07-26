package com.crypto.notify.model.notificationType;

import com.crypto.notify.constants.NotificationTypeConstants;
import com.crypto.notify.model.notificationBase.NonTargetNotificationModel;

import java.util.List;
import java.util.stream.IntStream;

public class DirectionChangeModel extends NonTargetNotificationModel {

    public DirectionChangeModel(String userId, String symbol, String time) {
        super(userId, symbol, time);
        this.type = NotificationTypeConstants.N_CHANGE;
    }

    private boolean consistent(long trend, long windowSize) {
        return trend >= windowSize * 0.75;
    }

    private int getTrend(List<Double> prices) {
        long rising = IntStream.range(1, prices.size())
                .filter(i -> prices.get(i) > prices.get(i - 1))
                .count();

        long falling = IntStream.range(1, prices.size())
                .filter(i -> prices.get(i) < prices.get(i - 1))
                .count();

        if (consistent(rising, prices.size())) {
            return 1; // Rising trend
        } else if (consistent(falling, prices.size())) {
            return -1; // Falling trend
        } else {
            return 0; // No clear trend
        }
    }

    @Override
    public double shouldNotify(List<Double> prices) {
        if (prices == null || prices.size() < 12) {
            return 0;
        }

        int prevTrend = getTrend( prices.subList( 0, prices.size()/2 ));
        int currTrend = getTrend( prices.subList( prices.size()/2, prices.size() ));

        if (prevTrend != currTrend && currTrend != 0) {
            return currTrend;
        }

        return 0;
    }

    @Override
    public String getNotificationTitle() {
        return String.format("%s price trend reversal!", this.symbol);
    }

    @Override
    public String getNotificationMessage(Object... params) {
        return String.format("In last hour the trend changed from %s to %s.",
                params[0].equals(1) ? "rising" : "falling",
                params[0].equals(1) ? "falling" : "rising");
    }
}
