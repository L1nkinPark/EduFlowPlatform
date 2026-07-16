package com.lms.frontend.service.impl;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CategoryResponse;
import com.lms.frontend.util.ConstantUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void testGetAllCategories_Success() {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategoryId(1L);
        categoryResponse.setCategoryName("Programming");

        ApiResponse<List<CategoryResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("SUCCESS");
        apiResponse.setPayload(Collections.singletonList(categoryResponse));

        ResponseEntity<ApiResponse> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        ApiResponse<List<CategoryResponse>> result = categoryService.getAllCategories(1, 10);

        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        assertEquals(1, result.getPayload().size());
        assertEquals("Programming", result.getPayload().get(0).getCategoryName());
    }

    @Test
    void testGetAllCategories_Failure_StatusNotSuccess() {
        ApiResponse<List<CategoryResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("ERROR");

        ResponseEntity<ApiResponse> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        ApiResponse<List<CategoryResponse>> result = categoryService.getAllCategories(1, 10);

        assertNull(result);
    }

    @Test
    void testGetAllCategories_Exception() {
        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenThrow(new RuntimeException("Connection error"));

        ApiResponse<List<CategoryResponse>> result = categoryService.getAllCategories(1, 10);

        assertNull(result);
    }
}
