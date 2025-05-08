package com.crypto.notify.controller;

import com.crypto.notify.service.KeyDbService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.*;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private final KeyDbService keyDbService;

    public DebugController(KeyDbService keyDbService) {
        this.keyDbService = keyDbService;
    }

    @GetMapping("/keydb")
    public Mono<String> getKeyValue(@RequestParam String key) {
        return keyDbService.getValue(key);
    }

    @PostMapping("/keydb")
    public Mono<Boolean> saveKeyValue(@RequestParam String key, @RequestParam String value) {
        return keyDbService.saveValue(key, value);
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