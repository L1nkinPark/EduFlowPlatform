package com.lms.backend.service.impl;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.request.AuthRequest;
import com.lms.backend.model.request.LoginRequest;
import com.lms.backend.model.request.RegisterRequest;
import com.lms.backend.model.response.AuthResponse;
import com.lms.backend.security.CustomUserDetails;
import com.lms.backend.security.JwtToken;
import com.lms.backend.service.AccountService;
import com.lms.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AccountService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Kiểm tra xem tên đăng nhập đã tồn tại chưa
        if (userService.checkUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken.");
        }
        // new User
        Account user = new Account();
        user.setFullName(request.getFullname().trim().toLowerCase());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setUsername(request.getUsername().trim().toLowerCase());
        user.setBirthday(request.getBirthday());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole().toUpperCase());
        user.setStatus(true);

        // Save
        userService.saveOrUpdate(user);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setFullname(user.getFullName());
        authResponse.setEmail(user.getEmail());
        authResponse.setBirthday(user.getBirthday());
        authResponse.setUsername(user.getUsername());
        authResponse.setRole(user.getRole());

        return authResponse;
    }



    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Account user = userService.findByUsername(request.getUsername());
        if (user != null) {
            CustomUserDetails userSecurity = new CustomUserDetails(user);

            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("username", user.getUsername());
            extraClaims.put("authorities", userSecurity.getAuthorities());

            String accessToken = jwtToken.generateToken(extraClaims, userSecurity);
            String refreshToken = jwtToken.generateRefreshToken(userSecurity);

            AuthResponse authResponse = new AuthResponse();
            authResponse.setFullname(user.getFullName());
            authResponse.setEmail(user.getEmail());
            authResponse.setBirthday(user.getBirthday());
            authResponse.setUsername(user.getUsername());
            authResponse.setRole(user.getRole());
            authResponse.setAccessToken(accessToken);
            authResponse.setRefreshToken(refreshToken);

            return authResponse;
        }

        return null;
    }

    @Override
    public AuthResponse refreshToken(AuthRequest request) {
        return null;
    }

}
