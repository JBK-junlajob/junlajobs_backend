package com.junlajobs_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
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

    public String deleteUser(String username) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(COLLECTION_USER).document(username).delete();

        return colApiFuture.get().getUpdateTime().toString();
    }

    public String updateUser(UserEntity user) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(COLLECTION_USER).document(user.getUsername()).set(user);

        return colApiFuture.get().getUpdateTime().toString();
    }

    public UserEntity getUserAccount(String username) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        DocumentReference documentReference = dbFireStore.collection(COLLECTION_USER).document(username);

        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document=future.get();

        UserEntity userEntity = null;
        if(document.exists()){
            userEntity = document.toObject(UserEntity.class);
            return userEntity;
        }else{
            return null;
        }
    }


}

