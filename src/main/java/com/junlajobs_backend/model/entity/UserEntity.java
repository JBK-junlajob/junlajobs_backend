package com.junlajobs_backend.model.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class UserEntity implements Serializable {
    private String username;
    private UserDetailEntity userDetail;
}
