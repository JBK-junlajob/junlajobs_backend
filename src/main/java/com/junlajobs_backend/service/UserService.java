package com.junlajobs_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.junlajobs_backend.exception.BaseException;
import com.junlajobs_backend.exception.UserException;
import com.junlajobs_backend.model.entity.UserDetailEntity;
import com.junlajobs_backend.model.entity.UserEntity;
import com.junlajobs_backend.model.request.LoginRequest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String COLLECTION_USER = "User";


    public String saveUser(UserEntity user) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        String passwordEncoded = passwordEncoder.encode(user.getUserDetail().getPassword());
        user.getUserDetail().setPassword(passwordEncoded);

        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(COLLECTION_USER).document(user.getUsername()).set(user.getUserDetail());

        return colApiFuture.get().getUpdateTime().toString();
    }

    public String deleteUser(String username) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(COLLECTION_USER).document(username).delete();

        return colApiFuture.get().getUpdateTime().toString();
    }

    public String updateUser(UserEntity user) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(COLLECTION_USER).document(user.getUsername()).set(user.getUserDetail());

        return colApiFuture.get().getUpdateTime().toString();
    }

    public UserEntity getUser(String username) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFireStore.collection(COLLECTION_USER).document(username);

        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = future.get();

        UserEntity userEntity = new UserEntity();
        if (document.exists()) {
            userEntity.setUsername(document.getId());
            userEntity.setUserDetail(document.toObject(UserDetailEntity.class));
            return userEntity;
        }

        return null;
    }

    public List<UserEntity> getUserList() throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        Iterable<DocumentReference> documentReference = dbFireStore.collection(COLLECTION_USER).listDocuments();
        Iterator<DocumentReference> iterator = documentReference.iterator();

        List<UserEntity> userEntityList = new ArrayList<>();

        while (iterator.hasNext()) {
            UserEntity userEntity = new UserEntity();

            DocumentReference document = iterator.next();
            ApiFuture<DocumentSnapshot> future = document.get();
            DocumentSnapshot snapshot = future.get();

            userEntity.setUsername(document.getId());
            userEntity.setUserDetail(snapshot.toObject(UserDetailEntity.class));
            userEntityList.add(userEntity);
        }

        return userEntityList;
    }

    @SneakyThrows
    public String login(LoginRequest loginRequest) throws BaseException {
        //TODO:update to encode and generate jwt
        if (loginRequest == null){
            throw UserException.loginRequestIsNull();
        }
        if(loginRequest.getPassword() == null){
            throw  UserException.loginPasswordIsNull();
        }
        UserEntity user = getUser(loginRequest.getUsername());
        if(user==null){
            throw UserException.loginFail();
        }else if(user !=null && passwordEncoder.matches(loginRequest.getPassword(),user.getUserDetail().getPassword())){
            return "login success";
        }
        throw UserException.loginFail();
    }


}

