package com.crypto.notify.controller;

import com.crypto.notify.dto.CryptoHistoryModel;
import com.crypto.notify.dto.CryptoModel;
import com.crypto.notify.service.KeyDbService;
import com.crypto.notify.constants.Constants;
import com.crypto.notify.util.CryptoDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/crypto")
@CrossOrigin(origins = "*")
public class CryptoController {
    private final KeyDbService keyDbService;
    private final Logger log = LoggerFactory.getLogger(CryptoController.class);

    public CryptoController(KeyDbService keyDbService) {
        this.keyDbService = keyDbService;
    }

    @GetMapping("/symbol/{querySymbol}")
    public Mono<CryptoModel> getCryptoBySymbol(@PathVariable String querySymbol) {
        return keyDbService.getValue(Constants.CRYPTO_PRICES)
                .flatMapMany(CryptoDTOMapper::toCrypto)
                .filter(cryptoPrice -> cryptoPrice.symbol().equalsIgnoreCase(querySymbol))
                .next();
    }

    @GetMapping("/list")
    public Flux<CryptoModel> list() {
        return keyDbService.getValue(Constants.CRYPTO_PRICES)
                .flatMapMany(CryptoDTOMapper::toCrypto);
    }

    @GetMapping("/history")
    public Flux<CryptoHistoryModel> history(@RequestParam String date) {
        return keyDbService.getFullList("chp-" + date)
                .map(CryptoDTOMapper::toCryptoHistory);
    }
}