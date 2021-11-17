package com.junlajobs_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.junlajobs_backend.helper.CollectionName;
import com.junlajobs_backend.model.entity.PostDetailEntity;
import com.junlajobs_backend.model.entity.PostEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class JobTypeService {


    public List<String> allType() {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        List<String> typeList = new ArrayList<>();
        Iterable<DocumentReference> documentReference = dbFireStore.collection(CollectionName.COLLECTION_JOBTYPE).listDocuments();
        Iterator<DocumentReference> iterator = documentReference.iterator();

        while (iterator.hasNext()) {
            DocumentReference document = iterator.next();
            typeList.add(document.getId());
        }

        return typeList;
    }

    public List<PostEntity> getPortfolioListByType(String type) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        CollectionReference portfolio = dbFireStore.collection(CollectionName.COLLECTION_Portfolio);
        Query query = portfolio.whereEqualTo("type", type);
        List<PostEntity> portfolioOfthisType = new ArrayList<>();

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            PostEntity postEntity = new PostEntity();

            postEntity.setPostname(document.getId());
            postEntity.setPostDetail(document.toObject(PostDetailEntity.class));
            portfolioOfthisType.add(postEntity);
        }

        return portfolioOfthisType;
    }
}
