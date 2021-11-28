package com.junlajobs_backend.service;

import com.google.firebase.auth.FirebaseAuth;
import com.junlajobs_backend.model.entity.UserDetailEntity;
import com.junlajobs_backend.model.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;


@Slf4j
@Service
public class EmailService {
    private JavaMailSender mailSender;
    private TemplateEngine templateEngine;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public ResponseEntity<String> sentVerify(String username, String email) {
        try {
            FirebaseAuth auth =FirebaseAuth.getInstance();
            Context ctx = new Context();
            ctx.setVariable("username", username);
            ctx.setVariable("link", auth.generateEmailVerificationLink(email));

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject("Verification");
            message.setTo(email);

            String content = templateEngine.process("VerifyEmailTemplate.html", ctx);
            message.setText(content, true);

            mailSender.send(mimeMessage);
            return ResponseEntity.ok("Successfully");
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    public ResponseEntity<String> sentForgotPassword(String username, String email, String fname, String lname) throws ExecutionException, InterruptedException {
        UserEntity user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        if(StringUtils.equals(username,user.getUsername())&&StringUtils.equals(email,user.getUserDetail().getEmail())&&StringUtils.equals(fname,user.getUserDetail().getFname())&&StringUtils.equals(lname,user.getUserDetail().getLname())){
            try {
                String temporaryPassword = this.generateString();
                Context ctx = new Context();
                ctx.setVariable("username", username);
                ctx.setVariable("newpass", temporaryPassword);

                UserDetailEntity userDetail = new UserDetailEntity();
                userDetail.setPassword(temporaryPassword);

                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                message.setSubject("ForgotPassword");
                message.setTo(email);

                String content = templateEngine.process("ForgotPasswordTemplate", ctx);
                message.setText(content, true);

                userService.editUser(userDetail);
                mailSender.send(mimeMessage);
                return ResponseEntity.ok("Successfully");
            } catch (Exception e) {
                log.error("Error: {}", e.getMessage(), e);
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.badRequest().body("data wrong");
    }


    public String generateString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        System.out.println("temporarypassword========================"+generatedString);
        return generatedString;
    }

}