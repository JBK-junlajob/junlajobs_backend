package com.junlajobs_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.junlajobs_backend.model.entity.PostDetailEntity;
import com.junlajobs_backend.model.entity.PostEntity;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class PostService {

    private static final String COLLECTION_Portfolio = "Portfolio";

    @Autowired
    private LikeService likeService;

    public String savePost(PostEntity post) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();
        post.getPostDetail().setCreator(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        post.getPostDetail().setRelease_date(Timestamp.now().toDate());
        post.getPostDetail().setLike(0);
        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(COLLECTION_Portfolio).document().create(post.getPostDetail());
        return colApiFuture.get().getUpdateTime().toString();
    }

    public String deletePost(String post) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(COLLECTION_Portfolio).document(post).delete();

        return colApiFuture.get().getUpdateTime().toString();
    }

    public String updatePost(PostEntity post) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(COLLECTION_Portfolio).document(post.getPostname()).set(post.getPostDetail());

        return colApiFuture.get().getUpdateTime().toString();
    }

    public PostEntity getPost(String postId) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFireStore.collection(COLLECTION_Portfolio).document(postId);

        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document=future.get();

        PostEntity postEntity= new PostEntity();
        if(document.exists()){
            postEntity.setPostname(document.getId());
            postEntity.setPostDetail(document.toObject(PostDetailEntity.class));
            postEntity.setUserAlreadyLike(likeService.userLikeThisPost(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString(),postId));
        }
        return postEntity;
    }

    public List<PostEntity> getPostList() throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        Iterable<DocumentReference> documentReference = dbFireStore.collection(COLLECTION_Portfolio).listDocuments();
        Iterator<DocumentReference> iterator=documentReference.iterator();

        List<PostEntity> postEntityList = new ArrayList<>();

        while (iterator.hasNext()){
            PostEntity postEntity= new PostEntity();

            DocumentReference document = iterator.next();
            ApiFuture<DocumentSnapshot> future = document.get();
            DocumentSnapshot snapshot = future.get();

            postEntity.setPostname(document.getId());
            postEntity.setPostDetail(snapshot.toObject(PostDetailEntity.class));
            postEntityList.add(postEntity);
        }

        return postEntityList;
    }

    @SneakyThrows
    public ResponseEntity<String> editPortfolio(PostEntity editor) {
        PostEntity thisPost = getPost(editor.getPostname());
        thisPost.getPostDetail().setLastUpdate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        //:TODO add error when user is not equal
        if(!username.equals(thisPost.getPostDetail().getCreator())){
            return ResponseEntity.badRequest().body("this account is not creator");
        }
        if (StringUtils.isNotBlank(editor.getPostDetail().getExplanation())) {
            thisPost.getPostDetail().setExplanation(editor.getPostDetail().getExplanation());
        }
        if (StringUtils.isNotBlank(editor.getPostDetail().getJob_title())) {
            thisPost.getPostDetail().setJob_title(editor.getPostDetail().getJob_title());
        }
        if (StringUtils.isNotBlank(editor.getPostDetail().getPrice_start())) {
            thisPost.getPostDetail().setPrice_start(editor.getPostDetail().getPrice_start());
        }
        if (StringUtils.isNotBlank(editor.getPostDetail().getPrice_end())) {
            thisPost.getPostDetail().setPrice_end(editor.getPostDetail().getPrice_end());
        }
        if (StringUtils.isNotBlank(editor.getPostDetail().getLatitude())) {
            thisPost.getPostDetail().setLatitude(editor.getPostDetail().getLatitude());
        }
        if (StringUtils.isNotBlank(editor.getPostDetail().getLongitude())) {
            thisPost.getPostDetail().setLongitude(editor.getPostDetail().getLongitude());
        }
        return ResponseEntity.ok(updatePost(thisPost));
    }
}
