package com.crypto.notify.controller;

import com.crypto.notify.dto.CryptoModel;
import com.crypto.notify.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/favorites")
    public Mono<List<CryptoModel>> userFavorites(@PathVariable String id) {
        return userService.getFavoriteCryptos(id);
    }

    @GetMapping("/{id}/notifications")
    public Mono<List<CryptoModel>> userNotifications(@PathVariable String id) {
        return userService.getMyNotificaitons(id);
    }
}