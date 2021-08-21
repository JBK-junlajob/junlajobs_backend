package com.junlajobs_backend.model;

import lombok.Data;

@Data
public class UserEntity {
    private String username;
    private String address;
    private String email;
    private String fname;
    private String lname;
    private String password;
    private String phone;
}
