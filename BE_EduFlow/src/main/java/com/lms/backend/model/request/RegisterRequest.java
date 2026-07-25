package com.lms.backend.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {


    @com.fasterxml.jackson.annotation.JsonAlias({"fullName", "fullname"})
    @NotBlank(message = "Full name is required")
    private String fullname;
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;
    private LocalDate birthday;
    private String role;
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 72, message = "Password must contain 6 to 72 characters")
    private String password;


}
