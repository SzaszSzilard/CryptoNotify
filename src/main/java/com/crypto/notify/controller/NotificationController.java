package com.crypto.notify.controller;

import com.crypto.notify.model.AboveNotificationModel;
import com.crypto.notify.service.KeyDbService;
import com.crypto.notify.service.NotificationService;
import com.crypto.notify.util.CryptoDTOMapper;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final KeyDbService keyDbService;
    private final CryptoDTOMapper cryptoDTOMapper;
    private final NotificationService notificationService;


    public NotificationController(KeyDbService keyDbService,
                                  CryptoDTOMapper cryptoDTOMapper,
                                  NotificationService notificationService) {
        this.keyDbService = keyDbService;
        this.cryptoDTOMapper = cryptoDTOMapper;
        this.notificationService = notificationService;
    }

    @PostMapping("/above")
    public Mono<Long> above(@RequestBody AboveNotificationModel aboveNotification) {
        return notificationService.saveNotification(aboveNotification);
    }

//    @PostMapping("/below")
//    public Mono<Object> () {
//        new AboveNotificationModel("userId", "symbol", 110000.0);
//        return keyDbService.saveUserNotification("userId", "notificationType", "notificationValues");
//    }
}