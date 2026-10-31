package com.crypto.notify.controller;

import com.crypto.notify.constants.Constants;
import com.crypto.notify.dto.CryptoHistoryModel;
import com.crypto.notify.dto.CryptoModel;
import com.crypto.notify.service.KeyDbService;
import com.crypto.notify.util.CryptoDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public CryptoModel getCryptoBySymbol(@PathVariable String querySymbol) {
        String rawPrices = keyDbService.getValue(Constants.CRYPTO_PRICES);
        return CryptoDTOMapper.toCrypto(rawPrices).stream()
                .filter(cryptoPrice -> cryptoPrice.symbol().equalsIgnoreCase(querySymbol))
                .findFirst()
                .orElse(null);
    }

    @GetMapping("/list")
    public List<CryptoModel> list() {
        String rawPrices = keyDbService.getValue(Constants.CRYPTO_PRICES);
        return CryptoDTOMapper.toCrypto(rawPrices);
    }

    @GetMapping("/history")
    public List<CryptoHistoryModel> history(@RequestParam String date) {
        return keyDbService.getFullList("chp-" + date).stream()
                .map(CryptoDTOMapper::toCryptoHistory)
                .toList();
    }
}
