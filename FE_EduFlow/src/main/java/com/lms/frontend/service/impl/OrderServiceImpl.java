package com.lms.frontend.service.impl;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.OrderService;
import com.lms.frontend.util.ConstantUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RestTemplate restTemplate;

    private final String apiUrl = ConstantUtil.HOST_URL + "/api/orders";

    private HttpEntity<?> getAuthorizedEntity() {
        HttpHeaders headers = new HttpHeaders();
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
    public ApiResponse<Void> createOrder(String courseId) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/create")
                    .queryParam("courseId", courseId);

            ResponseEntity<ApiResponse<Void>> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    getAuthorizedEntity(),
                    new ParameterizedTypeReference<ApiResponse<Void>>() {}
            );
            return responseEntity.getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ApiResponse<List<CourseResponse>> getUserCourses() {
        try {
            ResponseEntity<ApiResponse<List<CourseResponse>>> responseEntity = restTemplate.exchange(
                    apiUrl + "/my-courses",
                    HttpMethod.GET,
                    getAuthorizedEntity(),
                    new ParameterizedTypeReference<ApiResponse<List<CourseResponse>>>() {}
            );
            return responseEntity.getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean hasPurchased(String courseId) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/check-purchase")
                    .queryParam("courseId", courseId);

            ResponseEntity<ApiResponse<Boolean>> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    getAuthorizedEntity(),
                    new ParameterizedTypeReference<ApiResponse<Boolean>>() {}
            );
            ApiResponse<Boolean> response = responseEntity.getBody();
            if (response != null && response.getPayload() != null) {
                return response.getPayload();
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public ApiResponse<String> getVnPayUrl(String courseId, String redirectOrigin) {
        return getVnPayUrl(courseId, redirectOrigin, null);
    }

    @Override
    public ApiResponse<String> getVnPayUrl(String courseId, String redirectOrigin, String promoCode) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/vnpay-url")
                    .queryParam("courseId", courseId)
                    .queryParam("redirectOrigin", redirectOrigin);
            if (promoCode != null && !promoCode.isBlank()) {
                builder.queryParam("promoCode", promoCode);
            }

            ResponseEntity<ApiResponse<String>> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    getAuthorizedEntity(),
                    new ParameterizedTypeReference<ApiResponse<String>>() {}
            );
            return responseEntity.getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ApiResponse<Map<String, Object>> validatePromoCode(String courseId, String promoCode) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/promo/validate")
                    .queryParam("courseId", courseId)
                    .queryParam("promoCode", promoCode);

            ResponseEntity<ApiResponse<Map<String, Object>>> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    getAuthorizedEntity(),
                    new ParameterizedTypeReference<ApiResponse<Map<String, Object>>>() {}
            );
            return responseEntity.getBody();
        } catch (org.springframework.web.client.HttpClientErrorException ex) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
                return objectMapper.readValue(ex.getResponseBodyAsString(),
                        objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, Map.class));
            } catch (Exception e) {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ApiResponse<String> verifyVnPayCallback(Map<String, String> params) {
        try {
            ResponseEntity<ApiResponse<String>> responseEntity = restTemplate.exchange(
                    apiUrl + "/vnpay-callback",
                    HttpMethod.POST,
                    new HttpEntity<>(params, getAuthorizedEntity().getHeaders()),
                    new ParameterizedTypeReference<ApiResponse<String>>() {}
            );
            return responseEntity.getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
