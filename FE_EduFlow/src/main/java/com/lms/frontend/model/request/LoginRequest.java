package com.lms.frontend.model.request;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest implements Serializable {

    @NotBlank(message = "Username is required.")
    String username;

    @NotBlank(message = "Password is required.")
    String password;

}

