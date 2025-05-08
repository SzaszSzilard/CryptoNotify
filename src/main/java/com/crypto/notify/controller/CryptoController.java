package com.crypto.notify.controller;

import com.crypto.notify.dto.CryptoPriceHistoryModel;
import com.crypto.notify.dto.CryptoPriceModel;
import com.crypto.notify.service.KeyDbService;
import com.crypto.notify.util.CryptoDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@RestController
@RequestMapping("/api/crypto")
public class CryptoController {
    private final KeyDbService keyDbService;
    private final Logger log = LoggerFactory.getLogger(CryptoController.class);
    private final CryptoDTOMapper cryptoDTOMapper;

    public CryptoController(KeyDbService keyDbService,
                            CryptoDTOMapper cryptoDTOMapper) {
        this.keyDbService = keyDbService;
        this.cryptoDTOMapper = cryptoDTOMapper;
    }

    @GetMapping("/list")
    public Mono<List<CryptoPriceModel>> list() {
        return keyDbService.getValue("crypto_prices")
                .map(cryptoDTOMapper::toCryptoPrice);
    }

    @GetMapping("/history")
    public Flux<CryptoPriceHistoryModel> history(@RequestParam String date) {
        return keyDbService.getFullList("chp-" + date)
                .map(cryptoDTOMapper::toCryptoPriceHistory);
    }
}