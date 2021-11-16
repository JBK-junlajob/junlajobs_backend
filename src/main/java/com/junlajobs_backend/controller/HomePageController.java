package com.junlajobs_backend.controller;

import com.junlajobs_backend.model.entity.PostEntity;
import com.junlajobs_backend.service.HomePageService;
import com.junlajobs_backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/homepage")
public class HomePageController {

    @Autowired
    private HomePageService homePageService;


    @GetMapping("/{lastdoc}")
    public List<PostEntity> getForAnotherPage(@PathVariable String lastdoc) throws ExecutionException, InterruptedException {
        return homePageService.getListForPage(lastdoc);
    }
}
