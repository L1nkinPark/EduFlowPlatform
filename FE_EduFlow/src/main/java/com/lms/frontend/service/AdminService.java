package com.lms.frontend.service;

import com.lms.frontend.model.request.SignUpRequest;
import com.lms.frontend.model.response.AccountResponse;
import com.lms.frontend.model.response.AdminDashboardResponse;
import com.lms.frontend.model.response.ApiResponse;

import java.util.List;

public interface AdminService {

    ApiResponse<AdminDashboardResponse> getDashboard();

    ApiResponse<List<AccountResponse>> getAccountsByRole(String role);

    ApiResponse<?> createInstructor(SignUpRequest signUpRequest);

    ApiResponse<?> updateAccountStatus(long accountId, boolean status);
}
