package com.lms.frontend.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserLogin {

    @NotBlank(message = "Thông tin bắt buộc")
    @Email(message = "Email không phù hợp")
    private String email;

    @NotBlank(message = "Thông tin bắt buộc")
    @Size(min = 1, message = "Độ dài tối thiểu là 1")
    private String password;

    public UserLogin() {
    }

    public UserLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public @NotBlank(message = "Thông tin bắt buộc") @Email(message = "Email không phù hợp") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Thông tin bắt buộc") @Email(message = "Email không phù hợp") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Thông tin bắt buộc") @Size(min = 1, message = "Độ dài tối thiểu là 1") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Thông tin bắt buộc") @Size(min = 1, message = "Độ dài tối thiểu là 1") String password) {
        this.password = password;
    }
}
