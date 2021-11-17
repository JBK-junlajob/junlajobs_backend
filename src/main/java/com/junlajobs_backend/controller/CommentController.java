package com.junlajobs_backend.controller;

import com.junlajobs_backend.model.entity.CommentEntity;
import com.junlajobs_backend.model.request.CommentRequest;
import com.junlajobs_backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/createmaincomment")
    public String createComment(@RequestBody CommentRequest commentRequest) throws ExecutionException, InterruptedException {
        return commentService.createComment(commentRequest);
    }


    @GetMapping("/getcommentofpost/{postId}")
    public List<CommentEntity> getCommentOfThisPost(@PathVariable String postId) throws ExecutionException, InterruptedException {
        return commentService.getCommentOfPost(postId);
    }

    @GetMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable String commentId) throws ExecutionException, InterruptedException {
        return commentService.deleteComment(commentId);
    }


}
