package com.argueseye.service.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConnection {

//    @Bean
//    public FirebaseApp firebaseApp() throws IOException {
//
//        FileInputStream serviceAccount = new FileInputStream("/etc/secrets/firebase-service-account.json");
//
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .build();
//
//        return FirebaseApp.initializeApp(options);
//    }

}
