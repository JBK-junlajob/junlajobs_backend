package com.junlajobs_backend.model.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeEntity {
    private String user;
    private String postId;
}
