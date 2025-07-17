package com.crypto.notify.dto;

import com.crypto.notify.util.CryptoDTOMapper;

import java.util.List;

public record CryptoHistoryModel(String time, List<CryptoModel> priceList) {
    @Override
    @SuppressWarnings("NullableProblems")
    public String toString() {
        return CryptoDTOMapper.toJson(this);
    }
}