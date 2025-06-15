package com.crypto.notify.dto;

import java.util.List;

public record CryptoHistoryModel(String time, List<CryptoModel> priceList) {}