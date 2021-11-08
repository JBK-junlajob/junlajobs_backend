package com.junlajobs_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-con")
public class TestConnectionController {

    @GetMapping
    public String testGetConnection() {
        return "Connection is Success";
    }

    @PostMapping
    public String testPostConnection() {
        return "Connection is Success";
    }

    @GetMapping("/case-error")
    public ResponseEntity<String> testReturnError() {
        return ResponseEntity.badRequest().body("bad request naja");
    }
}
