package com.crypto.notify.service;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@Slf4j
@Service
public class KeyDbService {
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    public KeyDbService(ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    public Mono<Long> autoID(String key) {
        return reactiveRedisTemplate.opsForValue().increment(key);
    }

    public Mono<String> getValue(String key) {
        return reactiveRedisTemplate.opsForValue().get(key);
    }

    public Mono<Boolean> saveValue(String key, String value) {
        return reactiveRedisTemplate.opsForValue().set(key, value);
    }

    public Mono<Long> pushIntoList(String key, String value) {
        return reactiveRedisTemplate.opsForList().leftPush(key, value);
    }

    public Flux<String> getFullList(String key) {
        return reactiveRedisTemplate.opsForList().range(key, 0, -1);
    }
}
