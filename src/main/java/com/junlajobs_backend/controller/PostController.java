package com.junlajobs_backend.controller;

import com.junlajobs_backend.model.entity.PostEntity;
import com.junlajobs_backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/getpost/{postname}")
    public PostEntity getPost(@PathVariable String postname) throws ExecutionException, InterruptedException {
        return postService.getPost(postname);
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
}
