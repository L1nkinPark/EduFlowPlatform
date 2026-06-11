package com.lms.frontend.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserForgot {

    @NotBlank(message = "Thông tin bắt buộc")
    @Email(message = "Email không phù hợp")
    private String email;

    public UserForgot() {
    }

    public UserForgot(String email) {
        this.email = email;
    }

    public @NotBlank(message = "Thông tin bắt buộc") @Email(message = "Email không phù hợp") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Thông tin bắt buộc") @Email(message = "Email không phù hợp") String email) {
        this.email = email;
    }
}
