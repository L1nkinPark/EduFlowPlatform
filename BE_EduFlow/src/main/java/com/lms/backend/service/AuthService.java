package com.lms.backend.service;
import com.lms.backend.model.request.AuthRequest;
import com.lms.backend.model.request.LoginRequest;
import com.lms.backend.model.request.RegisterRequest;
import com.lms.backend.model.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(AuthRequest request);

}

