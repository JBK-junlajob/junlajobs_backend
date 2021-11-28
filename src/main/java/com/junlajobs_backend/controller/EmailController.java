package com.junlajobs_backend.controller;

import com.junlajobs_backend.model.request.ForgotPasswordRequest;
import com.junlajobs_backend.model.request.VerifyEmailRequest;
import com.junlajobs_backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;


    @PostMapping("/verify")
    public ResponseEntity<String> sendVerifyEmail(@RequestBody VerifyEmailRequest verifyEmailRequest) throws ExecutionException, InterruptedException {
        return emailService.sentVerify(verifyEmailRequest.getUsername(),verifyEmailRequest.getEmail());
    }

    @PostMapping("/forgotpassword")
    public ResponseEntity<String> sendForgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) throws ExecutionException, InterruptedException {
        return emailService.sentForgotPassword(forgotPasswordRequest.getUsername(),forgotPasswordRequest.getEmail(),forgotPasswordRequest.getFname(),forgotPasswordRequest.getLname());
    }

}
