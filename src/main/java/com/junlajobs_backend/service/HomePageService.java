package com.junlajobs_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.junlajobs_backend.model.entity.PostDetailEntity;
import com.junlajobs_backend.model.entity.PostEntity;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
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
public class HomePageService {
    private static final String COLLECTION_Portfolio = "Portfolio";


    public List<PostEntity> getListForPage(String lastdoc) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        DocumentReference documentReference = dbFireStore.collection(COLLECTION_Portfolio).document(lastdoc);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot lastDocSnap =future.get();

        Query query = null;

        if (StringUtils.isBlank(lastdoc)){
            query =  dbFireStore.collection(COLLECTION_Portfolio).orderBy("release_date", Query.Direction.DESCENDING).limit(10);
        }else if (StringUtils.isNotBlank(lastdoc)){
            query =  dbFireStore.collection(COLLECTION_Portfolio).orderBy("release_date", Query.Direction.DESCENDING).startAt(lastDocSnap).limit(10);
        }

        List<PostEntity> portfolioOfthisPage = new ArrayList<>();

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            PostEntity postEntity= new PostEntity();

            postEntity.setPostname(document.getId());
            postEntity.setPostDetail(document.toObject(PostDetailEntity.class));
            portfolioOfthisPage.add(postEntity);
        }

        return portfolioOfthisPage;
    }

}
