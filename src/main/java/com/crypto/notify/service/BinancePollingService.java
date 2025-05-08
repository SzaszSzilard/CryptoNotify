package com.crypto.notify.service;

import com.crypto.notify.dto.CryptoPriceHistoryModel;
import com.crypto.notify.dto.CryptoPriceModel;
import com.crypto.notify.util.CryptoDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BinancePollingService {

    private final WebClient webClient;
    private final KeyDbService keyDbService;
    private final CryptoDTOMapper cryptoDTOMapper;
    private final Logger log = LoggerFactory.getLogger(BinancePollingService.class);


    public BinancePollingService(WebClient.Builder webClientBuilder,
                                 KeyDbService keyDbService,
                                 CryptoDTOMapper cryptoDTOMapper) {
        this.webClient = webClientBuilder.baseUrl("https://api.binance.com").build();
        this.keyDbService = keyDbService;
        this.cryptoDTOMapper = cryptoDTOMapper;
    }

    @Scheduled(fixedRate = 1000)
    public void currentPricePoll() {
        webClient.get()
                .uri("api/v3/ticker/price")
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                        List<CryptoPriceModel> prices = cryptoDTOMapper.toCryptoPrice(response).stream()
                                .filter(price -> price.symbol().contains("USDT"))
                                .toList();

                        return keyDbService.saveValue("crypto_prices", cryptoDTOMapper.toJson(prices));
                })
                .subscribe(saved -> {
                    if (!saved) {
                        log.error("Failed to save Binance data");
                    }
                });
    }

    @Scheduled(fixedRate = 1000*60*5)
    public void historyPricePoll() {
        webClient.get()
                .uri("api/v3/ticker/price")
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    List<CryptoPriceModel> prices = cryptoDTOMapper.toCryptoPrice(response).stream()
                            .filter(price -> price.symbol().contains("USDT"))
                            .toList();

                    LocalDateTime now = LocalDateTime.now();
                    String key = "chp" + "-" + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String time = now.format(DateTimeFormatter.ofPattern("HH:mm"));

                    CryptoPriceHistoryModel cryptoPriceHistoryModell = new CryptoPriceHistoryModel(time, prices);

                    return keyDbService.pushIntoList(key, cryptoDTOMapper.toJson(cryptoPriceHistoryModell));
                })
                .subscribe(saved -> log.info("Binance data saved for history"));
    }
}