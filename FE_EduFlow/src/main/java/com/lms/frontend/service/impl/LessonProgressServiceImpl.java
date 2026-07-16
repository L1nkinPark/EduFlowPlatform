package com.lms.frontend.service.impl;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.LessonProgressService;
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

import java.util.List;

@Service
public class LessonProgressServiceImpl implements LessonProgressService {

    @Autowired
    private RestTemplate restTemplate;

    private final String apiUrl = ConstantUtil.HOST_URL + "/api/progress";

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
    public ApiResponse<Boolean> toggleProgress(Long lessonId) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/toggle")
                    .queryParam("lessonId", lessonId);

            ResponseEntity<ApiResponse<Boolean>> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    getAuthorizedEntity(),
                    new ParameterizedTypeReference<ApiResponse<Boolean>>() {}
            );
            return responseEntity.getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ApiResponse<List<Long>> getCompletedLessonIds(String courseId) {
        try {
            ResponseEntity<ApiResponse<List<Long>>> responseEntity = restTemplate.exchange(
                    apiUrl + "/course/" + courseId,
                    HttpMethod.GET,
                    getAuthorizedEntity(),
                    new ParameterizedTypeReference<ApiResponse<List<Long>>>() {}
            );
            return responseEntity.getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ApiResponse<Integer> getCourseProgressPercentage(String courseId) {
        try {
            ResponseEntity<ApiResponse<Integer>> responseEntity = restTemplate.exchange(
                    apiUrl + "/course/" + courseId + "/percentage",
                    HttpMethod.GET,
                    getAuthorizedEntity(),
                    new ParameterizedTypeReference<ApiResponse<Integer>>() {}
            );
            return responseEntity.getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
