package com.lms.frontend.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegister {

    @NotBlank(message = "Thông tin bắt buộc")
    @Size(min = 1, message = "Độ dài tối thiểu là 1")
    private String firstName;

    @NotBlank(message = "Thông tin bắt buộc")
    @Size(min = 1, message = "Độ dài tối thiểu là 1")
    private String lastName;

    @NotBlank(message = "Thông tin bắt buộc")
    @Size(min = 1, message = "Độ dài tối thiểu là 1")
    private String password;

    @NotBlank(message = "Thông tin bắt buộc")
    @Email(message = "Email không phù hợp")
    private String email;

    public UserRegister() {
    }

    public UserRegister(String firstName, String lastName, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public @NotBlank(message = "Thông tin bắt buộc") @Size(min = 1, message = "Độ dài tối thiểu là 1") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "Thông tin bắt buộc") @Size(min = 1, message = "Độ dài tối thiểu là 1") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank(message = "Thông tin bắt buộc") @Size(min = 1, message = "Độ dài tối thiểu là 1") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "Thông tin bắt buộc") @Size(min = 1, message = "Độ dài tối thiểu là 1") String lastName) {
        this.lastName = lastName;
    }

    public @NotBlank(message = "Thông tin bắt buộc") @Size(min = 1, message = "Độ dài tối thiểu là 1") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Thông tin bắt buộc") @Size(min = 1, message = "Độ dài tối thiểu là 1") String password) {
        this.password = password;
    }

    public @NotBlank(message = "Thông tin bắt buộc") @Email(message = "Email không phù hợp") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Thông tin bắt buộc") @Email(message = "Email không phù hợp") String email) {
        this.email = email;
    }
}
