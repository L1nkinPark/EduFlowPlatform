package com.lms.frontend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.lms.frontend.model.request.ChangePasswordRequest;
import com.lms.frontend.model.request.LoginRequest;
import com.lms.frontend.model.request.ProfileUpdateRequest;
import com.lms.frontend.model.request.SignUpRequest;
import com.lms.frontend.model.response.AccountResponse;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.AccountService;
import com.lms.frontend.util.ConstantUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private RestTemplate restTemplate;

    private String apiUrl = ConstantUtil.HOST_URL + "/api/auth/login";

    private final String accountApiUrl = ConstantUtil.HOST_URL + "/api/account";

    private HttpEntity<?> getAuthorizedEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpSession session = attributes.getRequest().getSession(false);
                if (session != null) {
                    AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
                    if (userLogin != null && userLogin.getAccessToken() != null) {
                        headers.setBearerAuth(userLogin.getAccessToken());
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return new HttpEntity<>(body, headers);
    }

    private <T> ApiResponse<T> parseErrorBody(RestClientResponseException ex) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(ex.getResponseBodyAsString(), ApiResponse.class);
        } catch (Exception e) {
            ApiResponse<T> apiResponse = new ApiResponse<>();
            apiResponse.error("Request failed: " + ex.getStatusCode());
            return apiResponse;
        }
    }

    private <T> ApiResponse<T> backendUnavailable() {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.error("Hệ thống đang tạm thời gián đoạn. Vui lòng thử lại sau.");
        return apiResponse;
    }

    @Override
    public ApiResponse<AccountResponse> getMyProfile() {
        try {
            ResponseEntity<ApiResponse<AccountResponse>> responseEntity = restTemplate.exchange(
                    accountApiUrl + "/me",
                    HttpMethod.GET,
                    getAuthorizedEntity(null),
                    new ParameterizedTypeReference<>() {
                    }
            );
            return responseEntity.getBody();
        } catch (RestClientResponseException ex) {
            return parseErrorBody(ex);
        } catch (RestClientException ex) {
            return backendUnavailable();
        }
    }

    @Override
    public ApiResponse<AccountResponse> updateMyProfile(ProfileUpdateRequest request) {
        try {
            ResponseEntity<ApiResponse<AccountResponse>> responseEntity = restTemplate.exchange(
                    accountApiUrl + "/me",
                    HttpMethod.PUT,
                    getAuthorizedEntity(request),
                    new ParameterizedTypeReference<>() {
                    }
            );
            return responseEntity.getBody();
        } catch (RestClientResponseException ex) {
            return parseErrorBody(ex);
        } catch (RestClientException ex) {
            return backendUnavailable();
        }
    }

    @Override
    public ApiResponse<?> changeMyPassword(ChangePasswordRequest request) {
        try {
            ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
                    accountApiUrl + "/me/password",
                    HttpMethod.PUT,
                    getAuthorizedEntity(request),
                    ApiResponse.class
            );
            return responseEntity.getBody();
        } catch (RestClientResponseException ex) {
            return parseErrorBody(ex);
        } catch (RestClientException ex) {
            return backendUnavailable();
        }
    }

    @Override
    public ApiResponse<?> deactivateMyAccount() {
        try {
            ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
                    accountApiUrl + "/me",
                    HttpMethod.DELETE,
                    getAuthorizedEntity(null),
                    ApiResponse.class
            );
            return responseEntity.getBody();
        } catch (RestClientResponseException ex) {
            return parseErrorBody(ex);
        } catch (RestClientException ex) {
            return backendUnavailable();
        }
    }

    @Override
    public ApiResponse register(SignUpRequest signUpRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<SignUpRequest> httpEntity = new HttpEntity<>(signUpRequest, headers);

            ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
                    ConstantUtil.HOST_URL + "/api/auth/register",
                    HttpMethod.POST,
                    httpEntity,
                    ApiResponse.class
            );

            return responseEntity.getBody();
        } catch (RestClientResponseException ex) {
            return parseErrorBody(ex);
        } catch (RestClientException ex) {
            return backendUnavailable();
        }
    }

    @Override
    public ApiResponse login(LoginRequest loginRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity httpEntity = new HttpEntity<>(loginRequest, headers);

            ResponseEntity<ApiResponse<AuthResponse>> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<>() {}
            );

            ApiResponse response = responseEntity.getBody();
            return response;
        } catch (RestClientResponseException ex) {
            return parseErrorBody(ex);
        } catch (RestClientException ex) {
            return backendUnavailable();
        }
    }

}

