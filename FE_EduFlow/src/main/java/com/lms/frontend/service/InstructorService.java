package com.lms.frontend.service;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.InstructorDashboardResponse;
import com.lms.frontend.model.response.InstructorOrderResponse;
import com.lms.frontend.model.response.InstructorStudentResponse;

import java.util.List;

public interface InstructorService {
    ApiResponse<InstructorDashboardResponse> getDashboard();

    ApiResponse<List<InstructorOrderResponse>> getOrders();

    ApiResponse<List<InstructorStudentResponse>> getStudents();
}
