package com.crypto.notify.service;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class KeyDbService {
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    public KeyDbService(ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    public Mono<Long> getIncId(String key) {
        return reactiveRedisTemplate.opsForValue().increment(key);
    }

    public Mono<String> getValue(String key) {
        return reactiveRedisTemplate.opsForValue().get(key);
    }

    public Mono<Boolean> saveValue(String key, String value) {
        return reactiveRedisTemplate.opsForValue().set(key, value);
    }

    public Mono<Boolean> deleteKey(String key) {
        return reactiveRedisTemplate.opsForValue().delete(key);
    }

    public Mono<Long> removeFromList(String key, String value) {
        return reactiveRedisTemplate.opsForList().remove(key, 1, value);
    }

    public Mono<Long> pushIntoList(String key, String value) {
        return reactiveRedisTemplate.opsForList().leftPush(key, value);
    }

    public Flux<String> getList(String key, int start, int end) {
        return reactiveRedisTemplate.opsForList().range(key, start, end);
    }

    public Flux<String> getFullList(String key) {
        return reactiveRedisTemplate.opsForList().range(key, 0, -1);
    }

    public Flux<String> getKeys(String pattern) {
        return reactiveRedisTemplate.keys(pattern);
    }

    public Flux<String> getAllCombinedKeys(String composedKey) {
        return reactiveRedisTemplate.keys(composedKey).flatMap(this::getFullList);
    }
}
