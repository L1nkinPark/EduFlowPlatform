package com.lms.frontend.service;


import com.lms.frontend.model.request.ChangePasswordRequest;
import com.lms.frontend.model.request.LoginRequest;
import com.lms.frontend.model.request.ProfileUpdateRequest;
import com.lms.frontend.model.request.SignUpRequest;
import com.lms.frontend.model.response.AccountResponse;
import com.lms.frontend.model.response.ApiResponse;

public interface AccountService {


    ApiResponse login(LoginRequest loginRequest);

    ApiResponse register(SignUpRequest signUpRequest);

    ApiResponse<AccountResponse> getMyProfile();

    ApiResponse<AccountResponse> updateMyProfile(ProfileUpdateRequest request);

    ApiResponse<?> changeMyPassword(ChangePasswordRequest request);

    ApiResponse<?> deactivateMyAccount();

}
