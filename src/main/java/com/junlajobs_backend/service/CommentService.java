package com.junlajobs_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.junlajobs_backend.model.entity.CommentEntity;
import com.junlajobs_backend.model.request.CommentRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CommentService {

    private static final String COLLECTION_MAIN_COMMENT = "Main_Comment";

    public String createComment(CommentRequest commentRequest) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();
        CommentEntity commentEntity = new CommentEntity();
                commentEntity.setComment(commentRequest.getComment());
                commentEntity.setPostId(commentRequest.getPostId());
                commentEntity.setRelease_date(Timestamp.now().toDate());
                commentEntity.setUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(COLLECTION_MAIN_COMMENT).document().create(commentEntity);
        return colApiFuture.get().getUpdateTime().toString();
    }

    public List<CommentEntity> getCommentOfPost(String postId) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        CollectionReference mainComment = dbFireStore.collection(COLLECTION_MAIN_COMMENT);
        Query query = mainComment.whereEqualTo("postId",postId);
        List<CommentEntity> commentOfPost = new ArrayList<>();

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            CommentEntity commentEntity = document.toObject(CommentEntity.class);
            commentOfPost.add(commentEntity);
        }
        return commentOfPost;
    }


    public String deleteComment(String commentId) throws ExecutionException, InterruptedException {
        Firestore dbFireStore = FirestoreClient.getFirestore();

        ApiFuture<WriteResult> colApiFuture = dbFireStore.collection(COLLECTION_MAIN_COMMENT).document(commentId).delete();

        return colApiFuture.get().getUpdateTime().toString();
    }

}
