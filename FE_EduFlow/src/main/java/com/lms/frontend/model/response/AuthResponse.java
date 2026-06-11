package com.lms.frontend.model.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AuthResponse {

    private String fullName;
    private String username;
    private String role;
    private String accessToken;
    private String refreshToken;
}
