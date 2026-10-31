package com.crypto.notify.service;

import com.crypto.notify.dto.CryptoModel;
import com.crypto.notify.model.notificationBase.NotificationModel;
import com.crypto.notify.model.notificationBase.PriceTargetNotificationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final KeyDbService keyDbService;
    private final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final String ID_COUNTER = "idc:";

    public NotificationService(KeyDbService keyDbService) {
        this.keyDbService = keyDbService;
    }

    public double priceTargetReached(PriceTargetNotificationModel notification, List<CryptoModel> cryptoPrices) {
        return cryptoPrices.stream()
                .filter(cryptoPrice -> cryptoPrice.symbol().equals(notification.getSymbol()))
                .findFirst()
                .map(CryptoModel::price)
                .map(List::of)
                .map(notification::shouldNotify)
                .orElse(0.0);
    }

    public Long save(NotificationModel notification) {
        String key = notification.getType() + ":" + notification.getUserId();
        Long id = keyDbService.getIncId(ID_COUNTER + key);
        notification.setId(id);
        return keyDbService.pushIntoList(key, notification.toString());
    }

    public Long delete(NotificationModel notification) {
        String key = notification.getType() + ":" + notification.getUserId();
        return keyDbService.removeFromList(key, notification.toString());
    }
}
