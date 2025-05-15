package com.crypto.notify.controller;

import com.crypto.notify.model.notificationBase.NotificationModel;
import com.crypto.notify.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @PostMapping("/")
    public Mono<Long> save(@RequestBody NotificationModel notification) {
        return notificationService.save(notification);
    }
}