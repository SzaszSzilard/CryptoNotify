package com.crypto.notify.controller;

import com.crypto.notify.dto.CryptoPriceHistoryModel;
import com.crypto.notify.service.KeyDbService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private final KeyDbService keyDbService;

    public DebugController(KeyDbService keyDbService) {
        this.keyDbService = keyDbService;
    }

    @GetMapping("/key/{key}")
    public Mono<String> key(@PathVariable String key) {
        return keyDbService.getValue(key);
    }

    @PostMapping("/key/{key}/{value}")
    public Mono<Boolean> key(@PathVariable String key, @PathVariable  String value) {
        return keyDbService.saveValue(key, value);
    }

    @GetMapping("/key-list/{key}")
    public Flux<String> keyList(@PathVariable String key) {
        return keyDbService.getFullList(key);
    }

    @DeleteMapping("/key/{key}")
    public Mono<Boolean> deleteKey(@PathVariable String key) {
        return keyDbService.deleteKey(key);
    }

    @GetMapping("/threads")
    public List<Map<String, String>> listAllThreads() {
        List<Map<String, String>> threads = new ArrayList<>();

        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            Map<String, String> threadInfo = new HashMap<>();
            threadInfo.put("name", thread.getName());
            threadInfo.put("id", String.valueOf(thread.getId()));
            threadInfo.put("state", thread.getState().toString());
            threadInfo.put("group", thread.getThreadGroup() != null ? thread.getThreadGroup().getName() : "null");
            threads.add(threadInfo);
        }

        threads.sort(Comparator.comparing(t -> t.get("name")));

        return threads;
    }
}