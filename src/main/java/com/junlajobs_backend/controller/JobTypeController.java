package com.junlajobs_backend.controller;

import com.junlajobs_backend.model.entity.PostEntity;
import com.junlajobs_backend.service.JobTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/jobtype")
public class JobTypeController {

    @Autowired
    private JobTypeService jobTypeService;

    @GetMapping("/listall")
    public List<String> listAll() throws ExecutionException, InterruptedException {
        return jobTypeService.allType();
    }

    @GetMapping("/getlistfromtype/{type}")
    public List<PostEntity> listPortFromType(@PathVariable(value = "type") String type) throws ExecutionException, InterruptedException {
        return jobTypeService.getPortfolioListByType(type);
    }

    @GetMapping("/categoryinthistype/{type}")
    public List<String> getCategoryInThisType(@PathVariable(value = "type") String type) throws ExecutionException, InterruptedException {
        return jobTypeService.getCategoryInThisType(type);
    }
}
