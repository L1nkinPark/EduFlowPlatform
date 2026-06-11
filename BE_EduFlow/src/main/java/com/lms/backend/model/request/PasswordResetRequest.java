package com.lms.backend.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {
//
    private String email;
    private String newPassword;

}
