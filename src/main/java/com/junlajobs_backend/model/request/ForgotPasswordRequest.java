package com.junlajobs_backend.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForgotPasswordRequest {
    private String username;
    private String email;
    private String fname;
    private String lname;
}
