package com.lms.frontend.service.impl;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
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
public class CourseServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CourseServiceImpl courseService;

    private CourseResponse courseResponse;

    @BeforeEach
    void setUp() {
        courseResponse = new CourseResponse();
        courseResponse.setCourseId("course123");
        courseResponse.setCourseName("Test Frontend Course");
        courseResponse.setPrice(79.0);
    }

    @Test
    void testGetAllCourses_Success() {
        ApiResponse<List<CourseResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("SUCCESS");
        apiResponse.setPayload(Collections.singletonList(courseResponse));
        ResponseEntity<ApiResponse> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        ApiResponse<List<CourseResponse>> result = courseService.getAllCourses(0, 10);

        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        assertEquals(1, result.getPayload().size());
        assertEquals("course123", result.getPayload().get(0).getCourseId());
        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testGetCoursesByAccount_Success() {
        ApiResponse<List<CourseResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("SUCCESS");
        apiResponse.setPayload(Collections.singletonList(courseResponse));
        ResponseEntity<ApiResponse<List<CourseResponse>>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        ApiResponse<List<CourseResponse>> result = courseService.getCoursesByAccount(1L);

        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        assertEquals(1, result.getPayload().size());
        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testGetCourseById_Success() {
        ApiResponse<CourseResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("SUCCESS");
        apiResponse.setPayload(courseResponse);
        ResponseEntity<ApiResponse<CourseResponse>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        ApiResponse<CourseResponse> result = courseService.getCourseById("course123");

        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        assertEquals("course123", result.getPayload().getCourseId());
        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testSaveCourse_Success() {
        ApiResponse<CourseResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("SUCCESS");
        apiResponse.setPayload(courseResponse);
        ResponseEntity<ApiResponse<CourseResponse>> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        ApiResponse<CourseResponse> result = courseService.saveCourse(courseResponse);

        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        assertEquals("course123", result.getPayload().getCourseId());
        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        );
    }
}
