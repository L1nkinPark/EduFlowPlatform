package com.lms.frontend.service.impl;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CategoryResponse;
import com.lms.frontend.service.CategoryService;
import com.lms.frontend.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private RestTemplate restTemplate;

    private String apiUrl = ConstantUtil.HOST_URL + "/api/categories";

    @Override
    public ApiResponse<List<CategoryResponse>> getAllCategories(int currentPage, int size) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("currentPage", currentPage);
            params.put("size", size);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.queryParam(entry.getKey(), entry.getValue());
            }

            ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            ApiResponse apiResponse = responseEntity.getBody();
            if (apiResponse != null && apiResponse.getStatus().equals("SUCCESS")) {
                return apiResponse;
            }

            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}