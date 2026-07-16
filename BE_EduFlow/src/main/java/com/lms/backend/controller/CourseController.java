
package com.lms.backend.controller;


import com.lms.backend.model.entity.Course;
import com.lms.backend.model.mapper.CourseMapper;
import com.lms.backend.model.request.CourseRequest;
import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.repository.CourseRepository;
import com.lms.backend.security.CustomUserDetails;
import com.lms.backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
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

    // API hiển thị danh sách tất cả khóa học
    @GetMapping
    public ResponseEntity<ApiResponse> getAllCourses(@RequestParam(defaultValue = "1") Integer currentPage,
              @RequestParam(defaultValue = "10") Integer size, @RequestParam(required = false) String keyword) {
        Pageable pageable = PageRequest.of(currentPage - 1, size);
        Page<Course> coursePage = null;

        if (keyword == null || keyword.trim().equalsIgnoreCase("")) {
            coursePage = courseSevice.getAllCourses(pageable);
        } else {
            coursePage = courseSevice.searchCourse(pageable, keyword);
        }

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

    //Add new Course
    @PostMapping
    public ResponseEntity<ApiResponse> saveCourse(@RequestBody CourseRequest courseRequest,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails){

        com.lms.backend.model.entity.Account instructor = userDetails != null ? userDetails.getAccount() : null;
        Course course = courseSevice.saveCourse(courseRequest, instructor);

        ApiResponse response = new ApiResponse();

        response.ok("OK", courseMapper.convertToDTO(course));

        return  ResponseEntity.ok(response);
    }


    //Update
    @PutMapping("/{courseId}")
    public ResponseEntity<ApiResponse> updateCourse(@RequestBody CourseRequest courseRequest, @PathVariable String courseId,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails){

        ApiResponse response = new ApiResponse();

        com.lms.backend.model.entity.Account instructor = userDetails != null ? userDetails.getAccount() : null;
        Course course = courseSevice.saveCourse(courseRequest, instructor);

        response.ok("OK", courseMapper.convertToDTO(course));

        return ResponseEntity.ok(response);
    }


    //Delete
    @DeleteMapping(value = "/{courseId}")
    public ResponseEntity<ApiResponse> deleteCourse(@PathVariable String courseId){
        boolean temp = courseSevice.deleteById(courseId);
        ApiResponse response = new ApiResponse();
        if(temp){
            response.ok("OK");
            return ResponseEntity.ok(response);
        }
        response.error("BAD_REQUEST", null);
        return ResponseEntity.ok(response);
    }
}

