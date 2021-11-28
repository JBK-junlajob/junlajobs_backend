package com.junlajobs_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.cloud.FirestoreClient;
import com.junlajobs_backend.exception.BaseException;
import com.junlajobs_backend.exception.UserException;
import com.junlajobs_backend.helper.CollectionName;
import com.junlajobs_backend.model.entity.UserDetailEntity;
import com.junlajobs_backend.model.entity.UserEntity;
import com.junlajobs_backend.model.request.LoginRequest;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Service
public class UserService {

    @Autowired
    private final TokenService tokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    public UserService(PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public String saveUser(UserEntity user) throws ExecutionException, InterruptedException {
        // check duplicate username
        if(this.getUser(user.getUsername())!=null){
            return null;
        }

        Firestore dbFireStore = FirestoreClient.getFirestore();

        String passwordEncoded = passwordEncoder.encode(user.getUserDetail().getPassword());
        user.getUserDetail().setPassword(passwordEncoded);

        emailService.sentVerify(user.getUsername(),user.getUserDetail().getEmail());
        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(CollectionName.COLLECTION_USER).document(user.getUsername()).set(user.getUserDetail());

        return colApiFuture.get().getUpdateTime().toString();
    }

    public String deleteUser(String username) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(CollectionName.COLLECTION_USER).document(username).delete();

        return colApiFuture.get().getUpdateTime().toString();
    }

    public String updateUser(UserEntity user) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(CollectionName.COLLECTION_USER).document(user.getUsername()).set(user.getUserDetail());

        return colApiFuture.get().getUpdateTime().toString();
    }

    public UserEntity getUser(String username) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFireStore.collection(CollectionName.COLLECTION_USER).document(username);

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

        Iterable<DocumentReference> documentReference = dbFireStore.collection(CollectionName.COLLECTION_USER).listDocuments();
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
        if (loginRequest == null) {
            throw UserException.loginRequestIsNull();
        }
        if (loginRequest.getPassword() == null) {
            throw UserException.loginPasswordIsNull();
        }
        UserEntity user = getUser(loginRequest.getUsername());
        if (user == null) {
            throw UserException.loginFail();
        } else if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getUserDetail().getPassword())) {
            return tokenService.tokenize(user);
        }
        throw UserException.loginFail();
    }


    public String loginViaEmail(String email) throws BaseException, ExecutionException, InterruptedException {
        UserEntity user = this.getUserByEmail(email);
        if (user == null) {
            throw UserException.loginFail();
        } else if (user != null ) {
            return tokenService.tokenize(user);
        }
        throw UserException.loginFail();
    }


    public UserEntity getUserByEmail(String email) throws BaseException, ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();
        CollectionReference collectionUser = dbFireStore.collection(CollectionName.COLLECTION_USER);

        Query query = collectionUser.whereEqualTo("email", email);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        UserEntity userEntity = new UserEntity();

        DocumentSnapshot document = querySnapshot.get().getDocuments().get(0);
        if (ObjectUtils.isNotEmpty(document)) {
            userEntity.setUsername(document.getId());
            userEntity.setUserDetail(document.toObject(UserDetailEntity.class));
            return userEntity;
        }

        return null;
    }

    public boolean checkOldPassword(String oldPass) throws BaseException, ExecutionException, InterruptedException {
        if (StringUtils.isBlank(oldPass)) {
            throw UserException.loginRequestIsNull();
        }
        String userPass = this.getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).getUserDetail().getPassword();

        if( passwordEncoder.matches(oldPass, userPass)) {
            return true;
        }
        return false;
    }

    @SneakyThrows
    public String editUser(UserDetailEntity detail) {
        FirebaseAuth auth =FirebaseAuth.getInstance();
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserEntity user = getUser(username);

        if (StringUtils.isNotBlank(detail.getAddress())) {
            user.getUserDetail().setAddress(detail.getAddress());
        }
        if (StringUtils.isNotBlank(detail.getEmail())) {
            user.getUserDetail().setEmail(detail.getEmail());
        }
        if (StringUtils.isNotBlank(detail.getFname())) {
            user.getUserDetail().setFname(detail.getFname());
        }
        if (StringUtils.isNotBlank(detail.getLname())) {
            user.getUserDetail().setLname(detail.getLname());
        }
        if (StringUtils.isNotBlank(detail.getPassword())) {
            user.getUserDetail().setPassword(passwordEncoder.encode(detail.getPassword()));
        }
        if (StringUtils.isNotBlank(detail.getPhone())) {
            user.getUserDetail().setPhone(detail.getPhone());
        }
        if (StringUtils.isNotBlank(detail.getProfilePicUrl())) {
            user.getUserDetail().setProfilePicUrl(detail.getProfilePicUrl());
        }
        if(ObjectUtils.isNotEmpty(auth.getUserByEmail(user.getUserDetail().getEmail()))){
            user.getUserDetail().setUid(auth.getUserByEmail(user.getUserDetail().getEmail()).getUid());
        }
        return updateUser(user);
    }

    public String testFirebaseAuth() throws FirebaseAuthException {
        FirebaseAuth auth =FirebaseAuth.getInstance();
        Firestore firestore = FirestoreClient.getFirestore();
        return "";
    }


}

