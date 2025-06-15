package com.crypto.notify.controller;

import com.crypto.notify.dto.CryptoPriceHistoryModel;
import com.crypto.notify.dto.CryptoPriceModel;
import com.crypto.notify.service.KeyDbService;
import com.crypto.notify.constants.Constants;
import com.crypto.notify.util.CryptoDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@RestController
@RequestMapping("/api/crypto")
@CrossOrigin(origins = "*")
public class CryptoController {
    private final KeyDbService keyDbService;
    private final Logger log = LoggerFactory.getLogger(CryptoController.class);
    private final CryptoDTOMapper cryptoDTOMapper;

    public CryptoController(KeyDbService keyDbService,
                            CryptoDTOMapper cryptoDTOMapper) {
        this.keyDbService = keyDbService;
        this.cryptoDTOMapper = cryptoDTOMapper;
    }

    @GetMapping("/symbol/{querySymbol}")
    public Mono<CryptoPriceModel> getCryptoBySymbol(@PathVariable String querySymbol) {
        return keyDbService.getValue(Constants.CRYPTO_PRICES)
                .map(cryptoDTOMapper::toCryptoPrice)
                .flatMapMany(Flux::fromIterable)
                .filter(cryptoPrice -> cryptoPrice.symbol().equalsIgnoreCase(querySymbol))
                .next();
    }

    @GetMapping("/list")
    public Mono<List<CryptoPriceModel>> list() {
        return keyDbService.getValue(Constants.CRYPTO_PRICES)
                .map(cryptoDTOMapper::toCryptoPrice);
    }

    @GetMapping("/history")
    public Flux<CryptoPriceHistoryModel> history(@RequestParam String date) {
        return keyDbService.getFullList("chp-" + date)
                .map(cryptoDTOMapper::toCryptoPriceHistory);
    }
}