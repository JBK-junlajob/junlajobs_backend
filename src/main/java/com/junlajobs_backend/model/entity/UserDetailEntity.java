package com.junlajobs_backend.model.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class UserDetailEntity implements Serializable {
    private String address;
    private String email;
    private String fname;
    private String lname;
    private String password;
    private String phone;
}
