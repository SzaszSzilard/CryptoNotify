package com.crypto.notify.model;

public abstract class NotificationModel{
    private Long id;
    private final String userId;
    private final String symbol;

    public NotificationModel(Long id, String userId, String symbol) {
        this.id = id;
        this.userId = userId;
        this.symbol = symbol;
    }

    public abstract void setAutoId(String notificationType);

    public String getUserId() {
        return userId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
