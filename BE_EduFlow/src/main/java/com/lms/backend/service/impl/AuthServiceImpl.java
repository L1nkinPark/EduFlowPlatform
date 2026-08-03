package com.lms.backend.service.impl;

import com.lms.backend.exception.ForbiddenException;
import com.lms.backend.exception.InvalidTokenException;
import com.lms.backend.exception.UnauthorizedException;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Locale;
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

    @Autowired
    private OTPServiceImpl otpService;

    // Roles that a caller may self-assign without being an admin.
    private static final Set<String> PUBLIC_SELF_REGISTER_ROLES = Set.of("STUDENT");

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Kiểm tra xem tên đăng nhập đã tồn tại chưa
        if (userService.checkUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        String requestedRole = request.getRole() == null ? "STUDENT" : request.getRole().toUpperCase();
        boolean adminCaller = isAdminCaller();

        if (!PUBLIC_SELF_REGISTER_ROLES.contains(requestedRole) && !adminCaller) {
            throw new ForbiddenException("Public registration only supports STUDENT accounts.");
        }

        if (userService.findByEmail(request.getEmail().trim().toLowerCase()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        if (!adminCaller && !otpService.consumeVerification(
                request.getEmail(), OTPServiceImpl.PURPOSE_SIGNUP, request.getOtpToken())) {
            throw new IllegalArgumentException("Email verification is required or has expired.");
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
        String identifier = request.getUsername().trim().toLowerCase(Locale.ROOT);
        Account user = userService.findByUsername(identifier);
        if (user == null) {
            user = userService.findByEmail(identifier).orElse(null);
        }
        if (user == null) {
            throw new UnauthorizedException("The username or password is incorrect.");
        }

        // Authenticate with the account's canonical username. Password-reset uses
        // email, so legacy accounts whose username differs from their email must
        // still be able to sign in with either identifier.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        request.getPassword()
                )
        );

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

    @Override
    public AuthResponse refreshToken(AuthRequest request) {
        try {
            String refreshToken = request.getAccessToken();
            String username = jwtToken.extractUsername(refreshToken);
            if (request.getUsername() != null && !request.getUsername().isBlank()
                    && !request.getUsername().equals(username)) {
                throw new InvalidTokenException("Refresh token does not belong to this user.");
            }

            Account user = userService.findByUsername(username);
            if (user == null || !user.isStatus()) {
                throw new InvalidTokenException("Refresh token user is unavailable.");
            }

            CustomUserDetails userDetails = new CustomUserDetails(user);
            if (!jwtToken.isRefreshToken(refreshToken)
                    || !jwtToken.isTokenValid(refreshToken, userDetails)) {
                throw new InvalidTokenException("Invalid or expired refresh token.");
            }

            Map<String, Object> claims = new HashMap<>();
            claims.put("username", user.getUsername());
            claims.put("authorities", userDetails.getAuthorities());

            AuthResponse response = toAuthResponse(user);
            response.setAccessToken(jwtToken.generateToken(claims, userDetails));
            response.setRefreshToken(jwtToken.generateRefreshToken(userDetails));
            return response;
        } catch (InvalidTokenException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InvalidTokenException("Invalid or expired refresh token.");
        }
    }

    private boolean isAdminCaller() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated()
                && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    private AuthResponse toAuthResponse(Account user) {
        AuthResponse response = new AuthResponse();
        response.setFullname(user.getFullName());
        response.setEmail(user.getEmail());
        response.setBirthday(user.getBirthday());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        return response;
    }

}
