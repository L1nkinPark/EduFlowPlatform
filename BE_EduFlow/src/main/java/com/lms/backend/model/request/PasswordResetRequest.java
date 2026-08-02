package com.lms.backend.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {
//
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "New password is required")
    @Size(min = 6, max = 72, message = "Password must contain 6 to 72 characters")
    private String newPassword;

    @NotBlank(message = "OTP verification token is required")
    private String otpToken;

}
