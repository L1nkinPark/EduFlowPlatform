package com.lms.frontend.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank
    private String fullName;

    private String email;

    private String username;;

    private String role;

    @NotBlank
    @Size(min = 6, max = 72)
    private String password;

    private String otpToken;
}
