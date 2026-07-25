package com.lms.frontend.service.impl;

import com.lms.frontend.model.request.ContactMessageRequest;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.service.ContactMessageService;
import com.lms.frontend.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ContactMessageServiceImpl implements ContactMessageService {

    @Autowired
    private RestTemplate restTemplate;

    private final String apiUrl = ConstantUtil.HOST_URL + "/api/contact-messages";

    @Override
    public ApiResponse<Map<String, Object>> submit(ContactMessageRequest request) {
        try {
            ResponseEntity<ApiResponse<Map<String, Object>>> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    new ParameterizedTypeReference<ApiResponse<Map<String, Object>>>() {
                    }
            );
            return response.getBody();
        } catch (Exception exception) {
            return null;
        }
    }
}
