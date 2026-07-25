package com.lms.backend.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username is required")
    private String username;

    // Do not enforce password length during sign-in. Existing accounts and every
    // password accepted at registration must always be allowed to authenticate.
    @NotBlank(message = "Password is required")
    private String password;


}
