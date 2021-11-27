package com.junlajobs_backend.controller;

import com.junlajobs_backend.model.entity.PostEntity;
import com.junlajobs_backend.model.entity.UserDetailEntity;
import com.junlajobs_backend.service.LikeService;
import com.junlajobs_backend.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;


    @PostMapping("/findport")
    public List<PostEntity> findPortfolio(@RequestBody PostEntity recuit) throws ExecutionException, InterruptedException {
        return recommendService.RecommendPort(recuit);
    }

    @PostMapping("/findrecruiter")
    public List<PostEntity> findrecruiter(@RequestBody PostEntity portfolio) throws ExecutionException, InterruptedException {
        return recommendService.RecommendRecruiter(portfolio);
    }

}
