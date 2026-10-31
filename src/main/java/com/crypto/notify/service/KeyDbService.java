package com.crypto.notify.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class KeyDbService {
    private static final Logger log = LoggerFactory.getLogger(KeyDbService.class);
    private final StringRedisTemplate stringRedisTemplate;

    public KeyDbService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public Long getIncId(String key) {
        return stringRedisTemplate.opsForValue().increment(key);
    }

    public String getValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public Boolean saveValue(String key, String value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.warn("KeyDB saveValue failed for key '{}': {}", key, e.getMessage());
            return false;
        }
    }

    public Boolean deleteKey(String key) {
        return stringRedisTemplate.delete(key);
    }

    public Long removeFromList(String key, String value) {
        return stringRedisTemplate.opsForList().remove(key, 1, value);
    }

    public Long pushIntoList(String key, String value) {
        return stringRedisTemplate.opsForList().leftPush(key, value);
    }

    public List<String> getList(String key, int start, int end) {
        List<String> list = stringRedisTemplate.opsForList().range(key, start, end);
        return list != null ? list : Collections.emptyList();
    }

    public List<String> getFullList(String key) {
        List<String> list = stringRedisTemplate.opsForList().range(key, 0, -1);
        return list != null ? list : Collections.emptyList();
    }

    public Set<String> getKeys(String pattern) {
        Set<String> keys = stringRedisTemplate.keys(pattern);
        return keys != null ? keys : Collections.emptySet();
    }

    public List<String> getAllCombinedKeys(String composedKey) {
        Set<String> keys = getKeys(composedKey);
        List<String> combined = new ArrayList<>();
        for (String key : keys) {
            combined.addAll(getFullList(key));
        }
        return combined;
    }
}
