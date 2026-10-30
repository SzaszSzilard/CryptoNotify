package com.crypto.notify.scheduler;

import com.crypto.notify.constants.Constants;
import com.crypto.notify.dto.CryptoHistoryModel;
import com.crypto.notify.dto.CryptoModel;
import com.crypto.notify.service.KeyDbService;
import com.crypto.notify.util.CryptoDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BinancePollingScheduler {

    private final RestClient restClient;
    private final KeyDbService keyDbService;
    private final Logger log = LoggerFactory.getLogger(BinancePollingScheduler.class);

    public BinancePollingScheduler(KeyDbService keyDbService) {
        this.keyDbService = keyDbService;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(1));
        requestFactory.setReadTimeout(Duration.ofSeconds(1));

        this.restClient = RestClient.builder()
                .requestFactory(requestFactory)
                .baseUrl("https://api.binance.com")
                .build();
    }

    @Scheduled(fixedRate = 1000)
    public void currentPricePoll() {
        try {
            String response = restClient.get()
                    .uri("/api/v3/ticker/price")
                    .retrieve()
                    .body(String.class);

            List<CryptoModel> prices = CryptoDTOMapper.toCrypto(response).stream()
                    .filter(price -> price.symbol().endsWith("USDT"))
                    .toList();

            boolean saved = Boolean.TRUE.equals(keyDbService.saveValue(Constants.CRYPTO_PRICES, prices.toString()));
            if (!saved) {
                log.error("Failed to save Binance data");
            }
        } catch (Exception e) {
            log.warn("Binance real time poll timeout/error: {}", e.toString());
        }
    }

    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void historyPricePoll() {
        try {
            String response = restClient.get()
                    .uri("/api/v3/ticker/price")
                    .retrieve()
                    .body(String.class);

            List<CryptoModel> prices = CryptoDTOMapper.toCrypto(response).stream()
                    .filter(price -> price.symbol().contains("USDT"))
                    .toList();

            LocalDateTime now = LocalDateTime.now();
            String key = "chp" + "-" + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String time = now.format(DateTimeFormatter.ofPattern("HH:mm"));

            Long saved = keyDbService.pushIntoList(key, new CryptoHistoryModel(time, prices).toString());
            log.info("Binance data saved for history index: {}", saved);
        } catch (Exception e) {
            log.warn("Binance history poll error: {}", e.toString());
        }
    }
}
