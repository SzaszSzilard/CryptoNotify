package com.crypto.notify.controller;

import com.crypto.notify.model.notificationBase.NotificationModel;
import com.crypto.notify.model.notificationTypes.PercentageAboveModel;
import com.crypto.notify.model.notificationTypes.PercentageBelowModel;
import com.crypto.notify.model.notificationTypes.PriceAboveModel;
import com.crypto.notify.model.notificationTypes.PriceBelowModel;
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

    // Debugging endpoints
    @PostMapping("/nabovet")
    public Mono<Boolean> a(@RequestBody PriceAboveModel notification) {
        return notificationService.priceTargetReached(notification);
    }

    @PostMapping("/nbelowt")
    public Mono<Boolean> b(@RequestBody PriceBelowModel notification) {
        return notificationService.priceTargetReached(notification);
    }

    @PostMapping("/npabovet")
    public Mono<Boolean> pa(@RequestBody PercentageAboveModel notification) {
        return notificationService.priceTargetReached(notification);
    }

    @PostMapping("/npbelowt")
    public Mono<Boolean> pb(@RequestBody PercentageBelowModel notification) {
        return notificationService.priceTargetReached(notification);
    }
}