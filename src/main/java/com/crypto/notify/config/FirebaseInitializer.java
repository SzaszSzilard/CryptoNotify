package com.crypto.notify.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.InputStream;

@Component
public class FirebaseInitializer {
    @PostConstruct
    public void init() throws Exception {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(
                        getClass().getResourceAsStream("/google/cryptonotify-64c29-firebase-adminsdk-fbsvc-5d1a6a3d71.json")))
                .build();

        FirebaseApp.initializeApp(options);
    }
}