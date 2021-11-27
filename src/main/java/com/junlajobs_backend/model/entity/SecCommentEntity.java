package com.junlajobs_backend.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SecCommentEntity {
    private String user;
    private String mainCommentId;
    private String comment;
    private Date release_date;
}
