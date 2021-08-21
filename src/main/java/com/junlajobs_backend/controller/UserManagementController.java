package com.junlajobs_backend.controller;

import com.junlajobs_backend.model.UserEntity;
import com.junlajobs_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/usermanagement")
public class UserManagementController {

    @Autowired
    private UserService userService;

    @PostMapping("/saveuser")
    public  String saveUser(@RequestBody UserEntity user) throws ExecutionException, InterruptedException {
        return userService.saveUser(user);
    }
}
