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


    @Override
    public double shouldNotify(List<Double> prices) {
        if (prices == null || prices.size() < 12) {
            return 0;
        }

        double threshold = 1.0;
        double gain = (prices.getLast() - prices.getFirst()) / prices.getFirst() * 100;

        long risingSteps = IntStream.range(1, prices.size())
                .filter(i -> prices.get(i) > prices.get(i - 1))
                .count();

        boolean strongEnough = gain > threshold || gain < -threshold;
        boolean consistent = risingSteps >= prices.size() * 0.75;
        boolean isRally = consistent && strongEnough;

        return isRally ? gain : 0;
    }

    @Override
    public String getNotificationTitle() {
        return String.format("%s is on a rally!", this.symbol);
    }

    @Override
    public String getNotificationMessage(Object... params) {
        return String.format(
                "In last hour it has %s %.2f%%.",
                ((double) params[0] > 0) ? "Risen" : "Fallen",
                Math.abs((double) params[0])
        );
    }
}
