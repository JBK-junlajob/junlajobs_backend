package com.junlajobs_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.junlajobs_backend.helper.CollectionName;
import com.junlajobs_backend.model.entity.MainCommentEntity;
import com.junlajobs_backend.model.entity.SecCommentEntity;
import com.junlajobs_backend.model.request.SecCommentRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CommentService {


    public String createMainComment(MainCommentEntity commentRequest) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();
        MainCommentEntity commentEntity = new MainCommentEntity();
        commentEntity.setComment(commentRequest.getComment());
        commentEntity.setPostId(commentRequest.getPostId());
        commentEntity.setRelease_date(Timestamp.now().toDate());
        commentEntity.setUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(CollectionName.COLLECTION_MAIN_COMMENT).document().create(commentEntity);
        return colApiFuture.get().getUpdateTime().toString();
    }

    public List<MainCommentEntity> getMainCommentOfPost(String postId) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        CollectionReference mainComment = dbFireStore.collection(CollectionName.COLLECTION_MAIN_COMMENT);
        Query query = mainComment.whereEqualTo("postId", postId);
        List<MainCommentEntity> commentOfPost = new ArrayList<>();

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            MainCommentEntity commentEntity = document.toObject(MainCommentEntity.class);
            commentOfPost.add(commentEntity);
        }
        return commentOfPost;
    }


    public String deleteMainComment(String commentId) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(CollectionName.COLLECTION_MAIN_COMMENT).document(commentId).delete();

        return colApiFuture.get().getUpdateTime().toString();
    }

    public String createSecComment(SecCommentRequest secCommentRequest) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();
        SecCommentEntity commentEntity = new SecCommentEntity ();
        commentEntity.setComment(secCommentRequest.getComment());
        commentEntity.setMainCommentId(secCommentRequest.getMainCommentId());
        commentEntity.setRelease_date(Timestamp.now().toDate());
        commentEntity.setUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(CollectionName.COLLECTION_SEC0NDARY_COMMENT).document().create(commentEntity);
        return colApiFuture.get().getUpdateTime().toString();
    }

    public List<SecCommentEntity> getSecComment(String mCommentid) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        CollectionReference mainComment = dbFireStore.collection(CollectionName.COLLECTION_SEC0NDARY_COMMENT);
        Query query = mainComment.whereEqualTo("mainCommentId", mCommentid);
        List<SecCommentEntity> secCommentEntities = new ArrayList<>();

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            SecCommentEntity commentEntity = document.toObject(SecCommentEntity.class);
            secCommentEntities.add(commentEntity);
        }
        return secCommentEntities;
    }


    public String deleteSecComment(String secCommentId) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(CollectionName.COLLECTION_SEC0NDARY_COMMENT).document(secCommentId).delete();

        return colApiFuture.get().getUpdateTime().toString();
    }

}
