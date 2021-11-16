package com.junlajobs_backend.controller;

import com.junlajobs_backend.model.entity.PostEntity;
import com.junlajobs_backend.service.HomePageService;
import com.junlajobs_backend.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;


    @GetMapping
    public ResponseEntity<String> likeThisPost(@PathVariable String postId) throws ExecutionException, InterruptedException {
        return likeService.likeThisPost(postId);
    }
}
