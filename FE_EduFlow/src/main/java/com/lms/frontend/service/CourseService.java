package com.lms.frontend.service;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;

import java.util.*;

public interface CourseService {
//
    ApiResponse<List<CourseResponse>> getAllCourses(int currentPage, int size);

    ApiResponse<CourseResponse> getCourseById(String courseId);

    ApiResponse<List<CourseResponse>> getCoursesByAccount(Long accountId);

//    Page<Course> searchCourse(Pageable pageable, String keyword);
//
//    Course
//
//    Course saveCourse(CourseRequest courseRequest);
//
//    boolean deleteById(String courseId);
}
