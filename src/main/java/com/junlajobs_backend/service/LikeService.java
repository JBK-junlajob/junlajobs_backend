package com.junlajobs_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.junlajobs_backend.helper.CollectionName;
import com.junlajobs_backend.model.entity.LikeEntity;
import com.junlajobs_backend.model.entity.PostEntity;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class LikeService {

    @Autowired
    private PostService postService;

    public ResponseEntity<String> likeThisPost(String postId) throws ExecutionException, InterruptedException {
        //ดึงข้อมูลเก่า
        PostEntity instance = postService.getPort(postId);
        int newTotalLike;

        //get current user
        String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        //check user alreadu like ?
        boolean likeOrNot = userLikeThisPost(user, postId);

        if (BooleanUtils.isTrue(likeOrNot)) {
            Firestore dbFireStore = FirestoreClient.getFirestore();

            CollectionReference like = dbFireStore.collection(CollectionName.COLLECTION_LIKE);
            Query query = like.whereEqualTo("user", user).whereEqualTo("postId", postId);
            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(CollectionName.COLLECTION_LIKE).document(document.getId()).delete();
            }

            newTotalLike = instance.getPostDetail().getLike() - 1;
            instance.getPostDetail().setLike(newTotalLike);
            postService.updatePort(instance);

            return ResponseEntity.ok("already unlike");
        }

        //set new total like and save
        newTotalLike = 1 + instance.getPostDetail().getLike();
        instance.getPostDetail().setLike(newTotalLike);
        postService.updatePort(instance);

        //save user and post id to collection like

        saveNewLike(user, postId);

        return ResponseEntity.ok("now you like this post");
    }

    public String saveNewLike(String user, String postId) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();
        LikeEntity likeEntity = LikeEntity.builder().user(user).postId(postId).build();

        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(CollectionName.COLLECTION_LIKE).document().create(likeEntity);
        return colApiFuture.get().getUpdateTime().toString();
    }

    public boolean userLikeThisPost(String user, String postId) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        CollectionReference like = dbFireStore.collection(CollectionName.COLLECTION_LIKE);
        Query query = like.whereEqualTo("user", user).whereEqualTo("postId", postId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (!querySnapshot.get().isEmpty()) {
            return true;
        }

        return false;
    }

}
