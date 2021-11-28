package com.junlajobs_backend.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyEmailRequest {
    private String username;
    private String email;
}
