package com.crypto.notify.dto;

import com.crypto.notify.util.CryptoDTOMapper;

public record CryptoModel(String symbol, Double price) {
    @Override
    @SuppressWarnings("NullableProblems")
    public String toString() {
        return CryptoDTOMapper.toJson(this);
    }
}