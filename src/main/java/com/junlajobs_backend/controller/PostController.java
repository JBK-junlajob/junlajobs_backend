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

    @PostMapping("/saveport")
    public  String savePost(@RequestBody PostEntity post) throws ExecutionException, InterruptedException {
        return postService.savePort(post);
    }

    @GetMapping("/getport/{postId}")
    public PostEntity getPost(@PathVariable String postId) throws ExecutionException, InterruptedException {
        return postService.getPort(postId);
    }

    @PostMapping("/updateport")
    public  String updatepost(@RequestBody PostEntity post) throws ExecutionException, InterruptedException {
        return postService.updatePort(post);
    }

    @GetMapping("/deleteport/{postname}")
    public  String deletePost(@PathVariable(value = "postname")String postname) throws ExecutionException, InterruptedException {
        return postService.deletePort(postname);
    }

    @GetMapping("/getallport")
    public List<PostEntity> getAllPost() throws ExecutionException, InterruptedException {
        return postService.getPortList();
    }

    @PostMapping("/editport")
    public ResponseEntity<String> editPost(@RequestBody PostEntity post) throws ExecutionException, InterruptedException {
        return postService.editPortfolio(post);
    }

    //recruit
    @PostMapping("/saverec")
    public  String saveRec(@RequestBody PostEntity post) throws ExecutionException, InterruptedException {
        return postService.saveRec(post);
    }

    @GetMapping("/getrec/{postId}")
    public PostEntity getRec(@PathVariable String postId) throws ExecutionException, InterruptedException {
        return postService.getRec(postId);
    }

    @PostMapping("/updaterec")
    public  String updateRec(@RequestBody PostEntity post) throws ExecutionException, InterruptedException {
        return postService.updateRec(post);
    }

    @GetMapping("/deleterec/{postname}")
    public  String deleteRec(@PathVariable(value = "postname")String postname) throws ExecutionException, InterruptedException {
        return postService.deleteRec(postname);
    }

    @GetMapping("/getallrec")
    public List<PostEntity> getAllRec() throws ExecutionException, InterruptedException {
        return postService.getRecList();
    }

    @PostMapping("/editrec")
    public ResponseEntity<String> editRec(@RequestBody PostEntity post) throws ExecutionException, InterruptedException {
        return postService.editRec(post);
    }


}
