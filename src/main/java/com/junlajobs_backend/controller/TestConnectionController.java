package com.junlajobs_backend.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test-con")
public class TestConnectionController {

    @GetMapping
    public String testGetConnection(){
        return "Connection is Success";
    }

    @PostMapping
    public String testPostConnection(){
        return "Connection is Success";
    }
}
