package com.crypto.notify.service;

import com.crypto.notify.dto.CryptoModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final KeyDbService keyDbService;

    public UserService(KeyDbService keyDbService) {
        this.keyDbService = keyDbService;
    }

    public Mono<List<CryptoModel>> getFavoriteCryptos(String userId) {
//        keyDbService.getValue("user_favorites_" + userId)
//                .flatMapMany(favorites -> {
//                    if (favorites == null || favorites.isEmpty()) {
//                        return Flux.empty();
//                    }
//                    String[] favoriteIds = favorites.split(",");
//                    return Flux.fromArray(favoriteIds);
//                })
//                .flatMap(keyDbService::getValue)
//                .map(value -> CryptoPriceModel.fromJson(value))
//                .collectList()
//                .switchIfEmpty(Mono.just(List.of()));

        return Mono.just(new ArrayList<>());
    }

    public Mono<List<CryptoModel>> getMyNotificaitons(String userId) {
        return Mono.just(new ArrayList<>());
    }
}
