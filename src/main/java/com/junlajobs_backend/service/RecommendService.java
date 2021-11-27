package com.junlajobs_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.junlajobs_backend.helper.CollectionName;
import com.junlajobs_backend.model.entity.PostDetailEntity;
import com.junlajobs_backend.model.entity.PostEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class RecommendService {

    @Autowired
    private JobCategoryService categoryService;

    public List<PostEntity> RecommendPort(PostEntity post) throws ExecutionException, InterruptedException {
        List<PostEntity> recommendpost = new ArrayList<>();


        Firestore dbFireStore = FirestoreClient.getFirestore();

        CollectionReference portfolio = dbFireStore.collection(CollectionName.COLLECTION_Portfolio);
        Query query = portfolio.whereEqualTo("category",post.getPostDetail().getCategory()).whereLessThanOrEqualTo("price_end",post.getPostDetail().getPrice_end());

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            PostEntity postEntity= new PostEntity();

            postEntity.setPostname(document.getId());
            postEntity.setPostDetail(document.toObject(PostDetailEntity.class));
            recommendpost.add(postEntity);
        }

        return recommendpost;
    }

    public List<PostEntity> RecommendRecruiter(PostEntity post) throws ExecutionException, InterruptedException {
        List<PostEntity> recommendpost = new ArrayList<>();


        Firestore dbFireStore = FirestoreClient.getFirestore();

        CollectionReference portfolio = dbFireStore.collection(CollectionName.COLLECTION_RECRUIT);
        Query query = portfolio.whereEqualTo("category",post.getPostDetail().getCategory()).whereGreaterThanOrEqualTo("price_start",post.getPostDetail().getPrice_start());

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            PostEntity postEntity= new PostEntity();

            postEntity.setPostname(document.getId());
            postEntity.setPostDetail(document.toObject(PostDetailEntity.class));
            recommendpost.add(postEntity);
        }

        return recommendpost;
    }


}
