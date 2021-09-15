package com.junlajobs_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.junlajobs_backend.model.entity.PostDetailEntity;
import com.junlajobs_backend.model.entity.PostEntity;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PostService {
    private static final String COLLECTION_POST = "Post";

    public String savePost(PostEntity post) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(COLLECTION_POST).document(post.getPostname()).set(post.getPostDetail());

        return colApiFuture.get().getUpdateTime().toString();
    }

    public String deletePost(String post) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(COLLECTION_POST).document(post).delete();

        return colApiFuture.get().getUpdateTime().toString();
    }

    public String updatePost(PostEntity post) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(COLLECTION_POST).document(post.getPostname()).set(post.getPostDetail());

        return colApiFuture.get().getUpdateTime().toString();
    }

    public PostEntity getPost(String post) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFireStore.collection(COLLECTION_POST).document(post);

        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document=future.get();

        PostEntity postEntity= new PostEntity();
        if(document.exists()){
            postEntity.setPostname(document.getId());
            postEntity.setPostDetail(document.toObject(PostDetailEntity.class));

        }
        return postEntity;
    }

    public List<PostEntity> getPostList() throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        Iterable<DocumentReference> documentReference = dbFireStore.collection(COLLECTION_POST).listDocuments();
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
}
