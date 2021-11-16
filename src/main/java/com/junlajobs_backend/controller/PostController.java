package com.junlajobs_backend.controller;

import com.junlajobs_backend.model.entity.PostEntity;
import com.junlajobs_backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/savepost")
    public  String savePost(@RequestBody PostEntity post) throws ExecutionException, InterruptedException {
        return postService.savePost(post);
    }

    @GetMapping("/getpost/{postId}")
    public PostEntity getPost(@PathVariable String postId) throws ExecutionException, InterruptedException {
        return postService.getPost(postId);
    }

    @PostMapping("/updatepost")
    public  String updatepost(@RequestBody PostEntity post) throws ExecutionException, InterruptedException {
        return postService.updatePost(post);
    }

    @GetMapping("/deletepost/{postname}")
    public  String deletePost(@PathVariable(value = "postname")String postname) throws ExecutionException, InterruptedException {
        return postService.deletePost(postname);
    }

    @GetMapping("/getallpost")
    public List<PostEntity> getAllPost() throws ExecutionException, InterruptedException {
        return postService.getPostList();
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editPost(@RequestBody PostEntity post) throws ExecutionException, InterruptedException {
        return postService.editPortfolio(post);
    }

}
