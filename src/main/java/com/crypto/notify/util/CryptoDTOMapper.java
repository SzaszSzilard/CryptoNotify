package com.crypto.notify.util;

import com.crypto.notify.dto.CryptoHistoryModel;
import com.crypto.notify.dto.CryptoModel;
import com.crypto.notify.model.notificationBase.NonTargetNotificationModel;
import com.crypto.notify.model.notificationBase.NotificationModel;
import com.crypto.notify.model.notificationBase.PriceTargetNotificationModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.List;

public class CryptoDTOMapper {
    private static final Logger log = LoggerFactory.getLogger(CryptoDTOMapper.class);
    private static final ObjectMapper jackson = new ObjectMapper().findAndRegisterModules();

    public static <T> String toJson(T cryptoPriceDTOs) {
        try {
            return jackson.writeValueAsString(cryptoPriceDTOs);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error mapping CryptoPriceDTO to Json", e);
        }
    }

    public static Flux<CryptoModel> toCrypto(String json) {
        return Flux.fromIterable(importGeneric(json, new TypeReference<List<CryptoModel>>() {}));
    }

    public static CryptoHistoryModel toCryptoHistory(String json) {
        return importGeneric(json, new TypeReference<>() {});
    }

    private static <T> T importGeneric(String json, TypeReference<T> ref) {
        try {
            return jackson.readValue(json, ref);
        } catch (JsonProcessingException e) {
            log.error("Failed to map JSON: {}", json, e);
            throw new RuntimeException("JSON mapping failed", e);
        }
    }

    public static NonTargetNotificationModel toNonTargetNotification(String json) {
        return importGeneric(json, new TypeReference<>() {});
    }

    public static PriceTargetNotificationModel toPriceTarget(String json) {
        return importGeneric(json, new TypeReference<>() {});
    }

    public static NotificationModel toNotification(String json) {
        return importGeneric(json, new TypeReference<>() {});
    }

}
