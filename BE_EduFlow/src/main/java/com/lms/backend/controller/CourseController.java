
package com.lms.backend.controller;


import com.lms.backend.exception.ForbiddenException;
import com.lms.backend.exception.ResourceNotFoundException;
import com.lms.backend.exception.UnauthorizedException;
import com.lms.backend.model.entity.Course;
import com.lms.backend.model.mapper.CourseMapper;
import com.lms.backend.model.request.CourseRequest;
import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.repository.CourseRepository;
import com.lms.backend.security.CustomUserDetails;
import com.lms.backend.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseSevice;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseRepository courseRepository;

    // API hiển thị danh sách tất cả khóa học
    @GetMapping
    public ResponseEntity<ApiResponse> getAllCourses(@RequestParam(defaultValue = "1") Integer currentPage,
              @RequestParam(defaultValue = "10") Integer size,
              @RequestParam(required = false) String keyword,
              @RequestParam(required = false) Long categoryId,
              @RequestParam(required = false) Long subCategoryId) {
        Pageable pageable = PageRequest.of(currentPage - 1, size);
        Page<Course> coursePage = courseSevice.filterCourses(
                pageable, keyword, categoryId, subCategoryId);

        ApiResponse response = new ApiResponse();

        response.ok("OK", courseMapper.convertToDTO(coursePage.getContent()));
        response.setPaginationMetadata(coursePage.getTotalElements(),
                coursePage.getTotalPages(),
                coursePage.getNumber(),
                coursePage.getSize());

        return ResponseEntity.ok(response);  // Trả về danh sách khóa học dưới dạng JSON
    }

    //Get by ID
    @GetMapping("/course")
    public ResponseEntity<ApiResponse> getCourseById(@RequestParam String courseId) {
        Course course = courseSevice.getCourseById(courseId);
        ApiResponse response = new ApiResponse();
        response.ok("OK", courseMapper.convertToDTO(course));
        return ResponseEntity.ok(response);
    }

    // Danh sách course thuộc về instructor đang đăng nhập (dùng cho trang "My Courses" của instructor).
    @GetMapping("/mine")
    public ResponseEntity<ApiResponse> getMyCourses(@AuthenticationPrincipal CustomUserDetails userDetails) {
        ApiResponse response = new ApiResponse();
        if (userDetails == null) {
            response.error("User not authenticated");
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body(response);
        }

        List<Course> courses = courseRepository.findByAccount(userDetails.getAccount());
        response.ok("OK", courseMapper.convertToDTO(courses));
        return ResponseEntity.ok(response);
    }

    //Add new Course
    @PostMapping
    public ResponseEntity<ApiResponse> saveCourse(@Valid @RequestBody CourseRequest courseRequest,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails){

        requireAuthenticated(userDetails);
        courseRequest.setCourseId(null);
        com.lms.backend.model.entity.Account instructor = userDetails.getAccount();
        Course course = courseSevice.saveCourse(courseRequest, instructor);

        ApiResponse response = new ApiResponse();

        response.ok("OK", courseMapper.convertToDTO(course));

        return  ResponseEntity.ok(response);
    }


    //Update
    @PutMapping("/{courseId}")
    public ResponseEntity<ApiResponse> updateCourse(@Valid @RequestBody CourseRequest courseRequest, @PathVariable String courseId,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails){

        ApiResponse response = new ApiResponse();

        assertCanManageCourse(courseId, userDetails);
        courseRequest.setCourseId(courseId);
        com.lms.backend.model.entity.Account instructor = userDetails.getAccount();
        Course course = courseSevice.saveCourse(courseRequest, instructor);

        response.ok("OK", courseMapper.convertToDTO(course));

        return ResponseEntity.ok(response);
    }


    //Delete
    @DeleteMapping(value = "/{courseId}")
    public ResponseEntity<ApiResponse> deleteCourse(@PathVariable String courseId,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails){
        assertCanManageCourse(courseId, userDetails);
        boolean temp = courseSevice.deleteById(courseId);
        ApiResponse response = new ApiResponse();
        if(temp){
            response.ok("OK");
            return ResponseEntity.ok(response);
        }
        response.error("BAD_REQUEST", null);
        return ResponseEntity.ok(response);
    }

    private void assertCanManageCourse(String courseId, CustomUserDetails userDetails) {
        requireAuthenticated(userDetails);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + courseId));
        boolean isAdmin = "ADMIN".equalsIgnoreCase(userDetails.getAccount().getRole());
        boolean isOwner = course.getAccount() != null
                && course.getAccount().getAccountId() == userDetails.getAccount().getAccountId();
        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("You do not own this course.");
        }
    }

    private void requireAuthenticated(CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getAccount() == null) {
            throw new UnauthorizedException("Authentication required.");
        }
    }
}
