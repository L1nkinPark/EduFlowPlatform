package com.lms.frontend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.frontend.model.request.LoginRequest;
import com.lms.frontend.model.request.SignUpRequest;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.util.ConstantUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AccountServiceImpl accountService;

    private SignUpRequest signUpRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest();
        signUpRequest.setFullName("John Doe");
        signUpRequest.setUsername("johndoe");
        signUpRequest.setEmail("john@example.com");
        signUpRequest.setPassword("password");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("johndoe");
        loginRequest.setPassword("password");
    }

    @Test
    void testRegister_Success() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus("SUCCESS");
        apiResponse.setMessage("Registration successful");

        ResponseEntity<ApiResponse> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(ConstantUtil.HOST_URL + "/api/auth/register"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(ApiResponse.class)
        )).thenReturn(responseEntity);

        ApiResponse result = accountService.register(signUpRequest);

        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        assertEquals("Registration successful", result.getMessage());
    }

    @Test
    void testRegister_Error() {
        String errorJson = "{\"status\":\"ERROR\",\"message\":\"Username already exists\"}";
        HttpClientErrorException exception = new HttpClientErrorException(
                HttpStatus.BAD_REQUEST, "Bad Request", errorJson.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8
        );

        when(restTemplate.exchange(
                eq(ConstantUtil.HOST_URL + "/api/auth/register"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(ApiResponse.class)
        )).thenThrow(exception);

        ApiResponse result = accountService.register(signUpRequest);

        assertNotNull(result);
        assertEquals("ERROR", result.getStatus());
        assertEquals("Username already exists", result.getMessage());
    }

    @Test
    void testLogin_Success() {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setFullName("John Doe");
        authResponse.setUsername("johndoe");
        authResponse.setRole("STUDENT");
        authResponse.setAccessToken("token123");

        ApiResponse<AuthResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("SUCCESS");
        apiResponse.setPayload(authResponse);

        ResponseEntity<ApiResponse<AuthResponse>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(ConstantUtil.HOST_URL + "/api/auth/login"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        ApiResponse result = accountService.login(loginRequest);

        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        assertNotNull(result.getPayload());
    }

    @Test
    void testLogin_Error() {
        String errorJson = "{\"status\":\"ERROR\",\"message\":\"Invalid credentials\"}";
        HttpClientErrorException exception = new HttpClientErrorException(
                HttpStatus.UNAUTHORIZED, "Unauthorized", errorJson.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8
        );

        when(restTemplate.exchange(
                eq(ConstantUtil.HOST_URL + "/api/auth/login"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenThrow(exception);

        ApiResponse result = accountService.login(loginRequest);

        assertNotNull(result);
        assertEquals("ERROR", result.getStatus());
        assertEquals("Invalid credentials", result.getMessage());
    }
}
