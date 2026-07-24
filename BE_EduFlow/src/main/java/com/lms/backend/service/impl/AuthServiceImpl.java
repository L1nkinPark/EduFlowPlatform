package com.lms.backend.service.impl;

import com.lms.backend.exception.ForbiddenException;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    // Roles that a caller may self-assign without being an admin.
    private static final Set<String> PUBLIC_SELF_REGISTER_ROLES = Set.of("STUDENT");

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Kiểm tra xem tên đăng nhập đã tồn tại chưa
        if (userService.checkUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken.");
        }

        String requestedRole = request.getRole() == null ? "STUDENT" : request.getRole().toUpperCase();

        // Chỉ ADMIN mới được tạo account với role khác STUDENT (INSTRUCTOR, ADMIN...).
        // Ngoại lệ: cho phép bootstrap ADMIN đầu tiên khi hệ thống chưa có ADMIN nào.
        boolean isBootstrapAdmin = "ADMIN".equals(requestedRole) && !userService.existsAdmin();
        if (!PUBLIC_SELF_REGISTER_ROLES.contains(requestedRole) && !isBootstrapAdmin && !isCallerAdmin()) {
            throw new ForbiddenException("Only an ADMIN account can create a " + requestedRole + " account.");
        }

        // new User
        Account user = new Account();
        user.setFullName(request.getFullname().trim().toLowerCase());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setUsername(request.getUsername().trim().toLowerCase());
        user.setBirthday(request.getBirthday());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(requestedRole);
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

    // Kiểm tra người gọi hiện tại (dựa trên JWT trong Authorization header) có phải ADMIN không.
    private boolean isCallerAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

}
