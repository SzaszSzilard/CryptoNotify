package com.crypto.notify.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {
    public static void sendPushNotification(String title, String body, String deviceToken) {
        Logger log = LoggerFactory.getLogger(NotificationService.class);

        try {
            Message message = Message.builder()
                    .putData("title", title)
                    .putData("body", body)
                    .setNotification(com.google.firebase.messaging.Notification.builder().setTitle(title).setBody(body).build())
                    .setToken(deviceToken)
                    .build();
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Push notification sent: {}", response);
        } catch (Exception e) {
            log.error("Failed to send push notification", e);
        }
    }
}
