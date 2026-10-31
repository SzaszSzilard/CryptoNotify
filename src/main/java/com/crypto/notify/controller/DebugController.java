package com.crypto.notify.controller;

import com.crypto.notify.service.KeyDbService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private final KeyDbService keyDbService;

    public DebugController(KeyDbService keyDbService) {
        this.keyDbService = keyDbService;
    }

    @GetMapping("/key/{key}")
    public String key(@PathVariable String key) {
        return keyDbService.getValue(key);
    }

    @PostMapping("/key/{key}/{value}")
    public Boolean key(@PathVariable String key, @PathVariable String value) {
        return keyDbService.saveValue(key, value);
    }

    @GetMapping("/key-list/{key}")
    public List<String> keyList(@PathVariable String key) {
        return keyDbService.getAllCombinedKeys(key);
    }

    @DeleteMapping("/key/{key}")
    public Boolean deleteKey(@PathVariable String key) {
        return keyDbService.deleteKey(key);
    }

    @GetMapping("/keys/")
    public List<String> keys() {
        return new ArrayList<>(keyDbService.getKeys("*"));
    }

    @GetMapping("/threads")
    public List<Map<String, String>> listAllThreads() {
        List<Map<String, String>> threads = new ArrayList<>();

        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            Map<String, String> threadInfo = new HashMap<>();
            threadInfo.put("name", thread.getName());
            threadInfo.put("id", String.valueOf(thread.threadId()));
            threadInfo.put("virtual", String.valueOf(thread.isVirtual()));
            threadInfo.put("state", thread.getState().toString());
            threadInfo.put("group", thread.getThreadGroup() != null ? thread.getThreadGroup().getName() : "null");
            threads.add(threadInfo);
        }

        threads.sort(Comparator.comparing(t -> t.get("name")));

        return threads;
    }
}
