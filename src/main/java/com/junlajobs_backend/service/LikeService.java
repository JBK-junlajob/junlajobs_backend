package com.junlajobs_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.junlajobs_backend.model.entity.LikeEntity;
import com.junlajobs_backend.model.entity.PostDetailEntity;
import com.junlajobs_backend.model.entity.PostEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class LikeService {

    private static final String COLLECTION_LIKE = "Like";

    @Autowired
    private PostService postService;

    public ResponseEntity<String> likeThisPost(String postId) throws ExecutionException, InterruptedException {
        //ดึงข้อมูลเก่าเอาlikeมาบวก
        PostEntity instance = postService.getPost(postId);
        int newTotalLike = 1+instance.getPostDetail().getLike();

        //set new total like and save
        instance.getPostDetail().setLike(newTotalLike);
        postService.updatePost(instance);

        //save user and post id to collection like
        String user = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        saveNewLike(user,postId);

        return ResponseEntity.ok("now you like this post");
    }

    public String saveNewLike(String user,String postId) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();
        LikeEntity likeEntity = LikeEntity.builder().user(user).postId(postId).build();

        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(COLLECTION_LIKE).document().create(likeEntity);
        return colApiFuture.get().getUpdateTime().toString();
    }

//    public boolean userLikeThisPost(String user,String postId){
//
//    }

}
