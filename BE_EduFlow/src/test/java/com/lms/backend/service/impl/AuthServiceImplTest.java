package com.lms.backend.service.impl;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.request.LoginRequest;
import com.lms.backend.model.request.RegisterRequest;
import com.lms.backend.model.response.AuthResponse;
import com.lms.backend.security.CustomUserDetails;
import com.lms.backend.security.JwtToken;
import com.lms.backend.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private AccountService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtToken jwtToken;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private Account account;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setFullname("John Doe");
        registerRequest.setEmail("john@test.com");
        registerRequest.setUsername("john");
        registerRequest.setRole("STUDENT");
        registerRequest.setPassword("password123");
        registerRequest.setBirthday(LocalDate.of(2000, 1, 1));

        loginRequest = new LoginRequest();
        loginRequest.setUsername("john");
        loginRequest.setPassword("password123");

        account = new Account();
        account.setAccountId(1L);
        account.setUsername("john");
        account.setFullName("john doe");
        account.setEmail("john@test.com");
        account.setRole("STUDENT");
        account.setPassword("encodedPassword");
        account.setStatus(true);
    }

    @Test
    void testRegister_Success() {
        when(userService.checkUsername("john")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userService.saveOrUpdate(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("john doe", response.getFullname());
        assertEquals("john@test.com", response.getEmail());
        assertEquals("john", response.getUsername());
        assertEquals("STUDENT", response.getRole());

        verify(userService, times(1)).checkUsername("john");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userService, times(1)).saveOrUpdate(any(Account.class));
    }

    @Test
    void testRegister_UsernameTaken() {
        when(userService.checkUsername("john")).thenReturn(Optional.of(account));

        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));

        verify(userService, times(1)).checkUsername("john");
        verify(userService, never()).saveOrUpdate(any(Account.class));
    }

    @Test
    void testLogin_Success() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userService.findByUsername("john")).thenReturn(account);
        when(jwtToken.generateToken(any(Map.class), any(CustomUserDetails.class))).thenReturn("accessToken123");
        when(jwtToken.generateRefreshToken(any(CustomUserDetails.class))).thenReturn("refreshToken123");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("john doe", response.getFullname());
        assertEquals("accessToken123", response.getAccessToken());
        assertEquals("refreshToken123", response.getRefreshToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, times(1)).findByUsername("john");
        verify(jwtToken, times(1)).generateToken(any(Map.class), any(CustomUserDetails.class));
        verify(jwtToken, times(1)).generateRefreshToken(any(CustomUserDetails.class));
    }

    @Test
    void testLogin_NotFound() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userService.findByUsername("john")).thenReturn(null);

        AuthResponse response = authService.login(loginRequest);

        assertNull(response);
        verify(userService, times(1)).findByUsername("john");
    }
}
