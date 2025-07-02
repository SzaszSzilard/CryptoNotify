package com.crypto.notify.model.notificationBase;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public abstract class NonTargetNotificationModel extends NotificationModel {
    private final String time;

    public NonTargetNotificationModel(String userId, String symbol, String time) {
        super(userId, symbol);
        this.time = time;
    }

    @JsonIgnore
    public String getTime() {
        return time;
    }

    public abstract double shouldNotify(List<Double> prices);
}