package com.crypto.notify.model.notificationBase;

import java.util.List;

public abstract class NonTargetNotificationModel extends NotificationModel {
    private final String time;

    public NonTargetNotificationModel(String userId, String symbol, String time) {
        super(userId, symbol);
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}