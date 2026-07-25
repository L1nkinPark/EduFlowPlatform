package com.lms.frontend.service.impl;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.PublicStatsResponse;
import com.lms.frontend.service.PublicStatsService;
import com.lms.frontend.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PublicStatsServiceImpl implements PublicStatsService {

    @Autowired
    private RestTemplate restTemplate;

    private final String apiUrl = ConstantUtil.HOST_URL + "/api/public/stats";

    @Override
    public ApiResponse<PublicStatsResponse> getPublicStats() {
        try {
            ResponseEntity<ApiResponse<PublicStatsResponse>> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ApiResponse<PublicStatsResponse>>() {}
            );
            return responseEntity.getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
