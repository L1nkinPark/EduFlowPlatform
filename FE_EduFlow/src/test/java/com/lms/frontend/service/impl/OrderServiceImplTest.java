package com.lms.frontend.service.impl;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
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
public class OrderServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderServiceImpl orderService;

    private CourseResponse courseResponse;

    @BeforeEach
    void setUp() {
        courseResponse = new CourseResponse();
        courseResponse.setCourseId("course123");
        courseResponse.setCourseName("Frontend Course");
        courseResponse.setPrice(49.0);
    }

    @Test
    void testCreateOrder_Success() {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("SUCCESS");
        ResponseEntity<ApiResponse<Void>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        ApiResponse<Void> result = orderService.createOrder("course123");

        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testGetUserCourses_Success() {
        ApiResponse<List<CourseResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("SUCCESS");
        apiResponse.setPayload(Collections.singletonList(courseResponse));
        ResponseEntity<ApiResponse<List<CourseResponse>>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        ApiResponse<List<CourseResponse>> result = orderService.getUserCourses();

        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        assertEquals(1, result.getPayload().size());
        assertEquals("course123", result.getPayload().get(0).getCourseId());
        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testHasPurchased_True() {
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("SUCCESS");
        apiResponse.setPayload(true);
        ResponseEntity<ApiResponse<Boolean>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        boolean result = orderService.hasPurchased("course123");

        assertTrue(result);
        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testHasPurchased_False() {
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("SUCCESS");
        apiResponse.setPayload(false);
        ResponseEntity<ApiResponse<Boolean>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        boolean result = orderService.hasPurchased("course123");

        assertFalse(result);
    }
}
