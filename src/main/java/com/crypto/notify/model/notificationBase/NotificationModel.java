package com.crypto.notify.model.notificationBase;

import com.crypto.notify.model.notificationTypes.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type" // JSON must include this field
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PriceAboveModel.class, name = "n_above"),
        @JsonSubTypes.Type(value = PriceBelowModel.class, name = "n_below"),
        @JsonSubTypes.Type(value = PercentageAboveModel.class, name = "n_percent_above"),
        @JsonSubTypes.Type(value = PercentageBelowModel.class, name = "n_percent_below"),
        @JsonSubTypes.Type(value = ShortTermRally.class, name = "n_rally")
})
public abstract class NotificationModel {
    @JsonIgnore
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
