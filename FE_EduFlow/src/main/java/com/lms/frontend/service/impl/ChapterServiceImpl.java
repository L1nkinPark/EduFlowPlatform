package com.lms.frontend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.frontend.model.request.ChapterRequest;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.model.response.ChapterResponse;
import com.lms.frontend.service.ChapterService;
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

@Service
public class ChapterServiceImpl implements ChapterService {

    @Autowired
    private RestTemplate restTemplate;

    private final String apiUrl = ConstantUtil.HOST_URL + "/api/chapters";

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
    public ApiResponse<ChapterResponse> createChapter(ChapterRequest request) {
        try {
            HttpEntity<ChapterRequest> entity = new HttpEntity<>(request, getAuthorizedHeaders());
            ResponseEntity<ApiResponse<ChapterResponse>> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
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
