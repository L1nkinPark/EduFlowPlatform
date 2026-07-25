package com.lms.frontend.service;

import com.lms.frontend.model.request.ContactMessageRequest;
import com.lms.frontend.model.response.ApiResponse;

import java.util.Map;

public interface ContactMessageService {
    ApiResponse<Map<String, Object>> submit(ContactMessageRequest request);
}
