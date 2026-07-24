package com.lms.frontend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.frontend.model.response.*;
import com.lms.frontend.service.InstructorService;
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

import java.util.List;

@Service
public class InstructorServiceImpl implements InstructorService {

    @Autowired
    private RestTemplate restTemplate;

    private final String apiUrl = ConstantUtil.HOST_URL + "/api/instructor";

    private HttpEntity<Void> getAuthorizedEntity() {
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
        return new HttpEntity<>(headers);
    }

    @Override
    public ApiResponse<InstructorDashboardResponse> getDashboard() {
        try {
            ResponseEntity<ApiResponse<InstructorDashboardResponse>> responseEntity = restTemplate.exchange(
                    apiUrl + "/dashboard",
                    HttpMethod.GET,
                    getAuthorizedEntity(),
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
    public ApiResponse<List<InstructorOrderResponse>> getOrders() {
        try {
            ResponseEntity<ApiResponse<List<InstructorOrderResponse>>> responseEntity = restTemplate.exchange(
                    apiUrl + "/orders",
                    HttpMethod.GET,
                    getAuthorizedEntity(),
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
    public ApiResponse<List<InstructorStudentResponse>> getStudents() {
        try {
            ResponseEntity<ApiResponse<List<InstructorStudentResponse>>> responseEntity = restTemplate.exchange(
                    apiUrl + "/students",
                    HttpMethod.GET,
                    getAuthorizedEntity(),
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
