package com.crypto.notify.model;

public class NotificationModel{
    private final String userId;
    private final String symbol;

    public NotificationModel(String userId, String symbol) {
        this.userId = userId;
        this.symbol = symbol;
    }
}
