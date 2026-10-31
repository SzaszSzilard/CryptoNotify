package com.crypto.notify.controller;

import com.crypto.notify.model.notificationBase.NotificationModel;
import com.crypto.notify.service.NotificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@CrossOrigin(origins = "*")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/")
    public Long save(@RequestBody NotificationModel notification) {
        return notificationService.save(notification);
    }

    @DeleteMapping("/")
    public Long delete(@RequestBody NotificationModel notification) {
        return notificationService.delete(notification);
    }
}
