package com.junlajobs_backend.model.request;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CommentRequest {

    private String postId;
    private String comment;

}
