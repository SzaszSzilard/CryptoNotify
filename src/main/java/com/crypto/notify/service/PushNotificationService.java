package com.crypto.notify.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {
    private static final Logger log = LoggerFactory.getLogger(PushNotificationService.class);

    public static void sendPushNotification(String title, String body, String deviceToken) {
        try {
            Message message = Message.builder()
                    .putData("title", title)
                    .putData("body", body)
                    .setNotification(Notification.builder().setTitle(title).setBody(body).build())
                    .setFid(deviceToken)
                    .build();
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Push notification sent: {}", response);
        } catch (Exception e) {
            log.error("Failed to send push notification", e);
        }
    }
}
