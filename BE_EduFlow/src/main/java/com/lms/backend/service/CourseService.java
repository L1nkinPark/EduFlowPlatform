package com.lms.backend.service;

import com.lms.backend.model.entity.Course;
import com.lms.backend.model.request.CourseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CourseService {
    // Hiển thị danh sách tất cả khóa học
    Page<Course> getAllCourses(Pageable pageable);

    Page<Course> searchCourse(Pageable pageable, String keyword);

    Course getCourseById(String courseId);

    Course saveCourse(CourseRequest courseRequest, com.lms.backend.model.entity.Account instructor);

    boolean deleteById(String courseId);
}
