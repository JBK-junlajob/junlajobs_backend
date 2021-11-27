package com.junlajobs_backend.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.junlajobs_backend.exception.BaseException;
import com.junlajobs_backend.model.entity.UserDetailEntity;
import com.junlajobs_backend.model.entity.UserEntity;
import com.junlajobs_backend.model.request.LoginRequest;
import com.junlajobs_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @PostMapping("/saveuser")
    public String saveUser(@RequestBody UserEntity user) throws ExecutionException, InterruptedException {
        return userService.saveUser(user);
    }

    @GetMapping("/getuser/{username}")
    public UserEntity getUser(@PathVariable String username) throws ExecutionException, InterruptedException {
        return userService.getUser(username);
    }

    @PostMapping("/updateuser")
    public String updateUser(@RequestBody UserEntity user) throws ExecutionException, InterruptedException {
        return userService.updateUser(user);
    }

    @GetMapping("/deleteuser/{username}")
    public String deleteUser(@PathVariable(value = "username") String username) throws ExecutionException, InterruptedException {
        return userService.deleteUser(username);
    }

    @GetMapping("/checkoldpassword/{oldpass}")
    public boolean checkOldPass(@PathVariable(value = "oldpass") String oldPass) throws ExecutionException, InterruptedException, BaseException {
        return userService.checkOldPassword(oldPass);
    }

    @GetMapping("/getalluser")
    public List<UserEntity> getAllUser() throws ExecutionException, InterruptedException {
        return userService.getUserList();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) throws BaseException {
        String response = userService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public String register(@RequestBody UserEntity user) throws ExecutionException, InterruptedException {
        return userService.saveUser(user);
    }

    @PostMapping("/edit")
    public String editUser(@RequestBody UserDetailEntity detail) {
        return userService.editUser(detail);
    }

    @GetMapping("/testauth")
    public String testauth() throws FirebaseAuthException {
        return userService.testFirebaseAuth();
    }

    @GetMapping("/getuserbyemail/{email}")
    public UserEntity getUserByEmail(@PathVariable(value = "email") String email) throws FirebaseAuthException, BaseException, ExecutionException, InterruptedException {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/loginbyemail/{email}")
    public String lolginByEmail(@PathVariable(value = "email") String email) throws FirebaseAuthException, BaseException, ExecutionException, InterruptedException {
        return userService.loginViaEmail(email);
    }


}
