package com.lms.frontend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.frontend.model.request.SignUpRequest;
import com.lms.frontend.model.response.AccountResponse;
import com.lms.frontend.model.response.AdminDashboardResponse;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.AdminService;
import com.lms.frontend.util.ConstantUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private RestTemplate restTemplate;

    private final String apiUrl = ConstantUtil.HOST_URL + "/api/admin";

    private HttpHeaders getAuthorizedHeaders() {
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
        return headers;
    }

    @Override
    public ApiResponse<AdminDashboardResponse> getDashboard() {
        try {
            HttpEntity<Void> entity = new HttpEntity<>(getAuthorizedHeaders());
            ResponseEntity<ApiResponse<AdminDashboardResponse>> responseEntity = restTemplate.exchange(
                    apiUrl + "/dashboard",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException ex) {
            return parseErrorBody(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ApiResponse<List<AccountResponse>> getAccountsByRole(String role) {
        try {
            HttpEntity<Void> entity = new HttpEntity<>(getAuthorizedHeaders());
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/accounts")
                    .queryParam("role", role);
            ResponseEntity<ApiResponse<List<AccountResponse>>> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException ex) {
            return parseErrorBody(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ApiResponse<?> createInstructor(SignUpRequest signUpRequest) {
        try {
            HttpEntity<SignUpRequest> entity = new HttpEntity<>(signUpRequest, getAuthorizedHeaders());
            ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
                    apiUrl + "/instructors",
                    HttpMethod.POST,
                    entity,
                    ApiResponse.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException ex) {
            return parseErrorBody(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ApiResponse<?> updateAccountStatus(long accountId, boolean status) {
        try {
            HttpEntity<Void> entity = new HttpEntity<>(getAuthorizedHeaders());
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(apiUrl + "/accounts/" + accountId + "/status")
                    .queryParam("status", status);
            ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.PUT,
                    entity,
                    ApiResponse.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException ex) {
            return parseErrorBody(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private <T> ApiResponse<T> parseErrorBody(HttpClientErrorException ex) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(ex.getResponseBodyAsString(), ApiResponse.class);
        } catch (Exception e) {
            ApiResponse<T> apiResponse = new ApiResponse<>();
            apiResponse.error("Request failed: " + ex.getStatusCode());
            return apiResponse;
        }
    }
}
