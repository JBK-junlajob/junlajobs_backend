package com.junlajobs_backend.controller;

import com.junlajobs_backend.model.entity.PostEntity;
import com.junlajobs_backend.service.JobCategoryService;
import com.junlajobs_backend.service.JobTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/jobcategory")
public class JobCategoryController {

    @Autowired
    private JobCategoryService jobCategoryService;

    @GetMapping("/listall")
    public List<String> listAll() throws ExecutionException, InterruptedException {
        return jobCategoryService.allCategory();
    }

    @GetMapping("/getlistfromcategory/{category}")
    public List<PostEntity> listPortFromType(@PathVariable(value = "category") String category) throws ExecutionException, InterruptedException {
        return jobCategoryService.getPortfolioListByCategory(category);
    }

}
