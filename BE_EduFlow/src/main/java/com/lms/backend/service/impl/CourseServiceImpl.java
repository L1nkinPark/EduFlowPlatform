package com.lms.backend.service.impl;

import com.lms.backend.model.entity.Course;
import com.lms.backend.model.request.CourseRequest;
import com.lms.backend.repository.CourseRepository;
import com.lms.backend.service.CourseService;
import com.lms.backend.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubCategoryService subCategoryService;

    @Override
    public Page<Course> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Override
    public Page<Course> searchCourse(Pageable pageable, String keyword) {
        return courseRepository.searchCourse(pageable, keyword);
    }

    @Override
    public Page<Course> filterCourses(Pageable pageable, String keyword, Long categoryId, Long subCategoryId) {
        String normalizedKeyword = keyword == null || keyword.isBlank() ? null : keyword.trim();
        return courseRepository.filterCourses(pageable, normalizedKeyword, categoryId, subCategoryId);
    }

    @Override
    public Course getCourseById(String courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            return courseOptional.get();
        } else {
            return null;
        }
    }

    @Override
    public Course saveCourse(CourseRequest courseRequest, com.lms.backend.model.entity.Account instructor) {
        Course course;
        boolean isNewCourse = courseRequest.getCourseId() == null
                || courseRequest.getCourseId().trim().isEmpty();

        if (isNewCourse) {
            course = new Course();
        } else {
            course = getCourseById(courseRequest.getCourseId());
        }

        if (course == null) {
            course = new Course();
        }

        course.setCourseName(courseRequest.getCourseName());
        course.setDescription(courseRequest.getDescription());
        course.setPrice(courseRequest.getPrice());
        course.setStatus(courseRequest.getStatus());
        course.setImage(courseRequest.getImage());
        course.setThumbnail(courseRequest.getThumbnail());
        course.setStartDate(courseRequest.getStartDate());
        course.setEndDate(courseRequest.getEndDate());
        com.lms.backend.model.entity.SubCategory subCategory =
                subCategoryService.findById(courseRequest.getSubCategoryId());
        if (subCategory == null) {
            throw new IllegalArgumentException("Selected course subcategory does not exist");
        }
        course.setSubCategory(subCategory);
        if (isNewCourse && instructor != null) {
            course.setAccount(instructor);
        }

        return courseRepository.save(course);
    }

    @Override
    public boolean deleteById(String courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course != null) {
            course.setStatus("Available");
            courseRepository.deleteById(courseId);
            return true;
        }
        return false;
    }
}
