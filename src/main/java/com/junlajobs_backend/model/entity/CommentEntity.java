package com.junlajobs_backend.model.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class CommentEntity {
    private String user;
    private String postId;
    private String comment;
    private Date release_date;
}
