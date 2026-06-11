package com.lms.backend.controller;

import com.lms.backend.model.request.AuthRequest;
import com.lms.backend.model.request.LoginRequest;
import com.lms.backend.model.request.RegisterRequest;
import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.model.response.AuthResponse;
import com.lms.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse authResponse = authService.register(request);

            ApiResponse apiResponse = new ApiResponse();
            apiResponse.ok(authResponse);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse authResponse = authService.login(request);

            ApiResponse apiResponse = new ApiResponse();
            apiResponse.ok(authResponse);
            System.out.println("AuthResponse created with accessToken: " + authResponse.getAccessToken());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody AuthRequest request) {
        try {
            AuthResponse authResponse = authService.refreshToken(request);

            ApiResponse apiResponse = new ApiResponse();
            apiResponse.ok(authResponse);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

}
