package com.crypto.notify.util;

import com.crypto.notify.dto.CryptoHistoryModel;
import com.crypto.notify.dto.CryptoModel;
import com.crypto.notify.model.notificationBase.NonTargetNotificationModel;
import com.crypto.notify.model.notificationBase.NotificationModel;
import com.crypto.notify.model.notificationBase.PriceTargetNotificationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.Collections;
import java.util.List;

public class CryptoDTOMapper {
    private static final Logger log = LoggerFactory.getLogger(CryptoDTOMapper.class);
    private static final JsonMapper jackson = JsonMapper.builder().findAndAddModules().build();

    public static <T> String toJson(T cryptoPriceDTOs) {
        try {
            return jackson.writeValueAsString(cryptoPriceDTOs);
        } catch (JacksonException e) {
            throw new RuntimeException("Error mapping CryptoPriceDTO to Json", e);
        }
    }

    public static List<CryptoModel> toCrypto(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        return importGeneric(json, new TypeReference<>() {});
    }

    public static CryptoHistoryModel toCryptoHistory(String json) {
        return importGeneric(json, new TypeReference<>() {});
    }

    private static <T> T importGeneric(String json, TypeReference<T> ref) {
        try {
            return jackson.readValue(json, ref);
        } catch (JacksonException e) {
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
