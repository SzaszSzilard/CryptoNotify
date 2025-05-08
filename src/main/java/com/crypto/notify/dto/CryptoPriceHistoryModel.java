package com.crypto.notify.dto;

import java.util.List;

public record CryptoPriceHistoryModel(String time, List<CryptoPriceModel> priceList) {}