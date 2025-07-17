package com.crypto.notify.controller;

import com.crypto.notify.model.notificationBase.NotificationModel;
import com.crypto.notify.service.KeyDbService;
import com.crypto.notify.util.CryptoDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final KeyDbService keyDbService;

    public UserController(KeyDbService keyDbService) {
        this.keyDbService = keyDbService;
    }

    @GetMapping("/{id}/notifications")
    public Flux<NotificationModel> userNotifications(@PathVariable String id) {
        return keyDbService.getKeys("*:" + id)
                .filter(key -> !key.startsWith("idc:"))
                .flatMap(key -> keyDbService.getFullList(key))
                .map(CryptoDTOMapper::toNotification);
    }
}