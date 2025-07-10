package com.crypto.notify.util;

import com.crypto.notify.dto.CryptoHistoryModel;
import com.crypto.notify.dto.CryptoModel;
import com.crypto.notify.model.notificationBase.NonTargetNotificationModel;
import com.crypto.notify.model.notificationBase.NotificationModel;
import com.crypto.notify.model.notificationBase.PriceTargetNotificationModel;
import com.crypto.notify.model.notificationType.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
public class CryptoDTOMapper extends ObjectMapper {
    private final Logger log = LoggerFactory.getLogger(CryptoDTOMapper.class);

    public CryptoDTOMapper() {
        super();
        this.findAndRegisterModules();
    }

    public <T> String toJson(T cryptoPriceDTOs) {
        try {
            return this.writeValueAsString(cryptoPriceDTOs);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error mapping CryptoPriceDTO to Json", e);
        }
    }

    public Flux<CryptoModel> toCrypto(String json) {
        return Flux.fromIterable(importGeneric(json, new TypeReference<List<CryptoModel>>() {}));
    }

    public CryptoHistoryModel toCryptoHistory(String json) {
        return importGeneric(json, new TypeReference<>() {});
    }

    private <T> T importGeneric(String json, TypeReference<T> ref) {
        try {
            return this.readValue(json, ref);
        } catch (JsonProcessingException e) {
            log.error("Failed to map JSON: {}", json, e);
            throw new RuntimeException("JSON mapping failed", e);
        }
    }

    public NonTargetNotificationModel toNonTargetNotification(String json) {
        return importGeneric(json, new TypeReference<>() {});
    }

    public PriceTargetNotificationModel toPriceTarget(String json) {
        return importGeneric(json, new TypeReference<>() {});
    }

    public NotificationModel toNotification(String json) {
        return importGeneric(json, new TypeReference<>() {});
    }

}
