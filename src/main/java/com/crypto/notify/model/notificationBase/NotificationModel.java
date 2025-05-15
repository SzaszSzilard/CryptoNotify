package com.crypto.notify.model.notificationBase;

import com.crypto.notify.model.notificationTypes.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type" // JSON must include this field
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PriceAboveModel.class, name = "priceAbove"),
        @JsonSubTypes.Type(value = PriceBelowModel.class, name = "priceBelow"),
        @JsonSubTypes.Type(value = PercentageAboveModel.class, name = "percentageAbove"),
        @JsonSubTypes.Type(value = PercentageBelowModel.class, name = "percentageBelow"),
        @JsonSubTypes.Type(value = ShortTermRally.class, name = "shortTermRally")
})
public abstract class NotificationModel {
    protected String type = "n_generic";
    protected Long id;
    protected final String userId;
    protected final String symbol;

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

    public String getType() {
        return type;
    }
}
