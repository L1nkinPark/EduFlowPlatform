package com.lms.frontend.service.impl;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.CourseService;
import com.lms.frontend.util.ConstantUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private RestTemplate restTemplate;

    private String apiUrl = ConstantUtil.HOST_URL + "/api/courses";

    private <K> HttpEntity<K> getAuthorizedEntity(K body) {
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

    @Override
    public ApiResponse<List<CourseResponse>> getAllCourses(int currentPage, int size) {
        return getAllCourses(currentPage, size, null);
    }

    @Override
    public ApiResponse<List<CourseResponse>> getAllCourses(int currentPage, int size, String keyword) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("currentPage", currentPage);
            params.put("size", size);
            if (keyword != null && !keyword.isBlank()) {
                params.put("keyword", keyword);
            }

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.queryParam(entry.getKey(), entry.getValue());
            }

            ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    getAuthorizedEntity(null),
                    new ParameterizedTypeReference<>() {
                    }
            );

            ApiResponse apiResponse = responseEntity.getBody();
            if (apiResponse != null && apiResponse.getStatus().equals("SUCCESS")) {
                return apiResponse;
            }

            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ApiResponse<List<CourseResponse>> getCoursesByAccount(Long accountId) {
        try {
            // Build the URL with `accountId` as a query parameter
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/account/courses")
                    .queryParam("accountId", accountId);

            // Send the request to get the list of courses associated with the account
            ResponseEntity<ApiResponse<List<CourseResponse>>> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    getAuthorizedEntity(null),
                    new ParameterizedTypeReference<ApiResponse<List<CourseResponse>>>() {}
            );

            // Process the response and return if successful
            ApiResponse<List<CourseResponse>> apiResponse = responseEntity.getBody();
            if (apiResponse != null && "SUCCESS".equals(apiResponse.getStatus())) {
                return apiResponse;
            }

            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    @Override
    public ApiResponse<CourseResponse> getCourseById(String courseId) {
        try {
            // Tạo URL với `courseId` dưới dạng tham số truy vấn
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/course")
                    .queryParam("courseId", courseId);

            ResponseEntity<ApiResponse<CourseResponse>> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    getAuthorizedEntity(null),
                    new ParameterizedTypeReference<ApiResponse<CourseResponse>>() {}
            );

            ApiResponse<CourseResponse> apiResponse = responseEntity.getBody();
            if (apiResponse != null && "SUCCESS".equals(apiResponse.getStatus())) {
                return apiResponse;
            }

            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ApiResponse<List<CourseResponse>> getMyCourses() {
        try {
            ResponseEntity<ApiResponse<List<CourseResponse>>> responseEntity = restTemplate.exchange(
                    apiUrl + "/mine",
                    HttpMethod.GET,
                    getAuthorizedEntity(null),
                    new ParameterizedTypeReference<ApiResponse<List<CourseResponse>>>() {}
            );

            ApiResponse<List<CourseResponse>> apiResponse = responseEntity.getBody();
            if (apiResponse != null && "SUCCESS".equals(apiResponse.getStatus())) {
                return apiResponse;
            }

            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ApiResponse<CourseResponse> saveCourse(CourseResponse courseResponse) {
        try {
            ResponseEntity<ApiResponse<CourseResponse>> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    getAuthorizedEntity(courseResponse),
                    new ParameterizedTypeReference<ApiResponse<CourseResponse>>() {}
            );

            return responseEntity.getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
