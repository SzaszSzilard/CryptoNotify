package com.crypto.notify.model.notificationBase;

import com.crypto.notify.service.NotificationService;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

//@JsonTypeInfo(
//        use = JsonTypeInfo.Id.NAME,
//        include = JsonTypeInfo.As.PROPERTY,
//        property = "type"
//)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = PriceTargetNotificationModel.class, name = "priceTarget"),
//        @JsonSubTypes.Type(value = PercentageChangeModel.class, name = "percentageChange")
//})
public abstract class NotificationModel {
    protected Long id;
    protected final String userId;
    protected final String symbol;

    @Autowired
    protected NotificationService notificationService;

    public NotificationModel(String userId, String symbol) {
        this.userId = userId;
        this.symbol = symbol;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public String getUserId() {
        return userId;
    }

    public String getSymbol() {
        return symbol;
    }

    public abstract Mono<Long> save();
}
