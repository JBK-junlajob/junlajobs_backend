package com.junlajobs_backend.controller;

import com.junlajobs_backend.model.entity.UserDetailEntity;
import com.junlajobs_backend.model.entity.UserEntity;
import com.junlajobs_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @PostMapping("/saveuser")
    public  String saveUser(@RequestBody UserEntity user) throws ExecutionException, InterruptedException {
        return userService.saveUser(user);
    }

    @GetMapping("/getuser/{username}")
    public UserDetailEntity getUser(@PathVariable String username) throws ExecutionException, InterruptedException {
        return userService.getUserAccount(username);
    }

    @PostMapping("/updateuser")
    public  String updateUser(@RequestBody UserEntity user) throws ExecutionException, InterruptedException {
        return userService.updateUser(user);
    }

    @GetMapping("/deleteuser/{username}")
    public  String deleteUser(@PathVariable(value = "username")String username) throws ExecutionException, InterruptedException {
        return userService.deleteUser(username);
    }


}
