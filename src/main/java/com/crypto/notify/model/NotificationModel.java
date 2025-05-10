package com.crypto.notify.model;

import com.crypto.notify.service.KeyDbService;

public class NotificationModel{
    private final KeyDbService keyDbService;

    private final Long id;
    private final String userId;
    private final String symbol;

    public NotificationModel(String userId, String symbol, KeyDbService keyDbService) {
        this.keyDbService = keyDbService;

        this.id = keyDbService.autoID("unseparatedNotityIds").block();
        this.userId = userId;
        this.symbol = symbol;
    }

    public String getUserId() {
        return userId;
    }

    public String getSymbol() {
        return symbol;
    }
}
