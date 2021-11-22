package com.junlajobs_backend.controller;

import com.junlajobs_backend.model.entity.MainCommentEntity;
import com.junlajobs_backend.model.entity.SecCommentEntity;
import com.junlajobs_backend.model.request.SecCommentRequest;
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
    public String createMainComment(@RequestBody MainCommentEntity commentRequest) throws ExecutionException, InterruptedException {
        return commentService.createMainComment(commentRequest);
    }


    @GetMapping("/getcommentofpost/{postId}")
    public List<MainCommentEntity> getCommentOfThisPost(@PathVariable String postId) throws ExecutionException, InterruptedException {
        return commentService.getMainCommentOfPost(postId);
    }

    @GetMapping("/delete/{commentId}")
    public String deleteMainComment(@PathVariable String commentId) throws ExecutionException, InterruptedException {
        return commentService.deleteMainComment(commentId);
    }

    //คอมเม้นรอง

    @PostMapping("/createseccomment")
    public String createSecComment(@RequestBody SecCommentRequest commentRequest) throws ExecutionException, InterruptedException {
        return commentService.createSecComment(commentRequest);
    }


    @GetMapping("/getseccomment/{mCommentid}")
    public List<SecCommentEntity> getSecComment(@PathVariable String mCommentid) throws ExecutionException, InterruptedException {
        return commentService.getSecComment(mCommentid);
    }

    @GetMapping("/deleteseccomment/{commentId}")
    public String deleteSecComment(@PathVariable String commentId) throws ExecutionException, InterruptedException {
        return commentService.deleteSecComment(commentId);
    }



}
