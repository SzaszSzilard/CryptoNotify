package com.crypto.notify.controller;

import com.crypto.notify.model.notificationTypes.PriceAboveModel;
import com.crypto.notify.model.notificationTypes.PriceBelowModel;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @PostMapping("/above")
    public Mono<Long> save(@RequestBody PriceAboveModel notification) {
        return notification.save();
    }

    @PostMapping("/below")
    public Mono<Long> save(@RequestBody PriceBelowModel notification) {
        return notification.save();
    }
}