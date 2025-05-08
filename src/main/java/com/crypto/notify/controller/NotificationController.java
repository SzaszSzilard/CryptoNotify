package com.crypto.notify.controller;

import com.crypto.notify.service.KeyDbService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final KeyDbService keyDbService;

    public NotificationController(KeyDbService keyDbService) {
        this.keyDbService = keyDbService;
    }

    @PostMapping("/notification")
    public Mono<Object> getBtcPrice() {
        return keyDbService.saveUserNotification("userId", "notificationType", "notificationValues");
    }
}