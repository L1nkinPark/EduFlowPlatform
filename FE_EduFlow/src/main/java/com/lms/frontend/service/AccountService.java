package com.lms.frontend.service;


import com.lms.frontend.model.request.LoginRequest;
import com.lms.frontend.model.response.ApiResponse;

public interface AccountService {


    ApiResponse login(LoginRequest loginRequest);

}
