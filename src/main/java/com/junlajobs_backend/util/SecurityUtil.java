package com.junlajobs_backend.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtil {

    private SecurityUtil(){

    }

    public static Optional<String> getCurrentUser(){
        SecurityContext context = SecurityContextHolder.getContext();
        if(context==null){
            return Optional.empty();
        }

        Authentication authentication = context.getAuthentication();
        if(authentication==null){
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal==null){
            return Optional.empty();
        }

        String username = (String) principal;
        return Optional.of(username);
    }
}
