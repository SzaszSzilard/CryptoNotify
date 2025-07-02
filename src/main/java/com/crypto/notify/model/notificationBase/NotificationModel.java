package com.crypto.notify.model.notificationBase;

import com.crypto.notify.constants.NotificationTypeConstants;
import com.crypto.notify.model.notificationType.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type" // JSON must include this field
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PriceAboveModel.class, name = NotificationTypeConstants.N_ABOVE),
        @JsonSubTypes.Type(value = PriceBelowModel.class, name = NotificationTypeConstants.N_BELOW),
        @JsonSubTypes.Type(value = PercentageAboveModel.class, name = NotificationTypeConstants.N_PERCENT_ABOVE),
        @JsonSubTypes.Type(value = PercentageBelowModel.class, name = NotificationTypeConstants.N_PERCENT_BELOW),
        @JsonSubTypes.Type(value = RallyModel.class, name = NotificationTypeConstants.N_RALLY)
})
public abstract class NotificationModel {
    @JsonIgnore
    protected String type = NotificationTypeConstants.N_GENERIC;
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
