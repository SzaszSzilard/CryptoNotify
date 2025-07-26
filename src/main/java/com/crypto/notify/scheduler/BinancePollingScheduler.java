package com.crypto.notify.scheduler;

import com.crypto.notify.dto.CryptoHistoryModel;
import com.crypto.notify.service.KeyDbService;
import com.crypto.notify.constants.Constants;
import com.crypto.notify.util.CryptoDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BinancePollingScheduler {

    private final WebClient webClient;
    private final KeyDbService keyDbService;
    private final Logger log = LoggerFactory.getLogger(BinancePollingScheduler.class);


    public BinancePollingScheduler(WebClient.Builder webClientBuilder,
                                   KeyDbService keyDbService) {
        this.keyDbService = keyDbService;
        ConnectionProvider provider = ConnectionProvider.builder("custom")
                .maxConnections(100)
                .pendingAcquireMaxCount(2000)
                .pendingAcquireTimeout(Duration.ofSeconds(1))
                .build();

        HttpClient httpClient = HttpClient.create(provider);

        this.webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("https://api.binance.com")
                .build();
    }

    @Scheduled(fixedRate = 1000)
    public void currentPricePoll() {
        webClient.get()
                .uri("api/v3/ticker/price")
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(1))
                .flatMapMany(CryptoDTOMapper::toCrypto)
                .filter(price -> price.symbol().endsWith("USDT"))
                .collectList()
                .flatMap(prices -> keyDbService.saveValue(Constants.CRYPTO_PRICES, prices.toString()))
                .onErrorResume(throwable -> {
                    log.warn("Binance real time poll timeout/error: {}", throwable.toString());
                    return Mono.empty();
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
                .flatMapMany(CryptoDTOMapper::toCrypto)
                .filter(price -> price.symbol().contains("USDT"))
                .collectList()
                .flatMap(prices -> {
                    LocalDateTime now = LocalDateTime.now();
                    String key = "chp" + "-" + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String time = now.format(DateTimeFormatter.ofPattern("HH:mm"));

                    return keyDbService.pushIntoList(key, new CryptoHistoryModel(time, prices).toString());
                })
                .onErrorResume(throwable -> {
                    log.warn("Binance history poll error: {}", throwable.toString());
                    return Mono.empty();
                })
                .subscribe(saved -> log.info("Binance data saved for history index: {}", saved));
    }
}