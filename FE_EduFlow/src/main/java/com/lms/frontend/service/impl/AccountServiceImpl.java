package com.lms.frontend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.lms.frontend.model.request.LoginRequest;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.AccountService;
import com.lms.frontend.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private RestTemplate restTemplate;

    private String apiUrl = ConstantUtil.HOST_URL + "/api/auth/login";


    @Override
    public ApiResponse login(LoginRequest loginRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity httpEntity = new HttpEntity<>(loginRequest, headers);

            ResponseEntity<ApiResponse<AuthResponse>> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<>() {}
            );

            ApiResponse response = responseEntity.getBody();
            return response;
        } catch (HttpClientErrorException ex) {
            try {
                if (ex.getStatusCode() != HttpStatus.OK) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ApiResponse apiResponse = objectMapper.readValue(ex.getResponseBodyAsString(), ApiResponse.class);
                    return apiResponse;
                }
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }

}

