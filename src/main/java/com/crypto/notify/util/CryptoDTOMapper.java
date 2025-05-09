package com.crypto.notify.util;

import com.crypto.notify.dto.CryptoPriceHistoryModel;
import com.crypto.notify.dto.CryptoPriceModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CryptoDTOMapper extends ObjectMapper {
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

    public List<CryptoPriceModel> toCryptoPrice(String json) {
        return importGeneric(json, new TypeReference<>() {});
    }

    public CryptoPriceHistoryModel toCryptoPriceHistory(String json) {
        return importGeneric(json, new TypeReference<>() {});
    }

    private <T> T importGeneric(String json, TypeReference<T> ref) {
        try {
            return this.readValue(json, ref);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON mapping failed", e);
        }
    }
}
