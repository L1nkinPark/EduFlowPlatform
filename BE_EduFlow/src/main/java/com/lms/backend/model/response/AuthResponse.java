package com.lms.backend.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    @JsonProperty("username")
    private String username;

    @JsonProperty("fullname")
    private String fullname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("role")
    private String role;

    @JsonProperty("birthday")
    private LocalDate birthday;

    @JsonProperty("accessToken")
    private String accessToken;


    @JsonProperty("refreshToken")
    private String refreshToken;

}
