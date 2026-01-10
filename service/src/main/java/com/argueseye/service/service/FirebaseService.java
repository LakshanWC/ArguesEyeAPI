package com.argueseye.service.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class FirebaseService {

    /*
    public Firestore getFireStore(){
        return FirestoreClient.getFirestore();
    }

    public void saveToFireStore(Map<String,Object> respond){
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        getFireStore().collection("server-status").document(timestamp).set(respond);
    }

     */
}
