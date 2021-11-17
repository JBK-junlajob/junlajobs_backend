package com.junlajobs_backend.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SecCommentRequest {

    private String mainCommentId ;
    private String comment;

}
