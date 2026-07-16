package com.lms.frontend.service;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;

import java.util.List;

public interface OrderService {
    ApiResponse<Void> createOrder(String courseId);
    ApiResponse<List<CourseResponse>> getUserCourses();
    boolean hasPurchased(String courseId);
}
