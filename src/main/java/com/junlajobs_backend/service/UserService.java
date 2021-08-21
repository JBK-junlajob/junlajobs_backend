package com.junlajobs_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.junlajobs_backend.model.UserEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;


@Service
public class UserService {

    private static final String COLLECTION_USER = "User";

    public String saveUser(UserEntity user) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(COLLECTION_USER).document(user.getUsername()).set(user);

        return colApiFuture.get().getUpdateTime().toString();
    }
}

