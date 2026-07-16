package com.lms.frontend.service;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;

import java.util.List;
import java.util.Map;

public interface OrderService {
    ApiResponse<Void> createOrder(String courseId);
    ApiResponse<List<CourseResponse>> getUserCourses();
    boolean hasPurchased(String courseId);
    ApiResponse<String> getVnPayUrl(String courseId, String redirectOrigin);
    ApiResponse<String> verifyVnPayCallback(Map<String, String> params);
}
