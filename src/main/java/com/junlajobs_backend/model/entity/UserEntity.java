package com.junlajobs_backend.model.entity;

import lombok.Data;

@Data
public class UserEntity  {
    private String username;
    private UserDetailEntity userDetail;
}
