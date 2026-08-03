package com.lms.frontend.model.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AuthResponse {

    @JsonAlias("fullname")
    private String fullName;
    private String email;
    private LocalDate birthday;
    private String username;
    private String role;
    private String accessToken;
    private String refreshToken;
}
