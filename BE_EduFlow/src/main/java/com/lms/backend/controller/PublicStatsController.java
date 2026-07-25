package com.lms.backend.controller;

import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.model.response.PublicStatsResponse;
import com.lms.backend.repository.AccountRepository;
import com.lms.backend.repository.CourseRepository;
import com.lms.backend.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Số liệu tổng quan công khai (không yêu cầu đăng nhập, không lộ doanh thu),
// dùng để thay thế các số liệu giả trên trang About/Landing.
@RestController
@RequestMapping("/api/public")
public class PublicStatsController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse> getPublicStats() {
        ApiResponse response = new ApiResponse();

        PublicStatsResponse stats = new PublicStatsResponse();
        stats.setTotalCourses(courseRepository.count());
        stats.setTotalInstructors(accountRepository.countByRole("INSTRUCTOR"));
        stats.setTotalStudents(accountRepository.countByRole("STUDENT"));
        stats.setTotalEnrollments(orderItemRepository.count());

        response.ok("OK", stats);
        return ResponseEntity.ok(response);
    }
}
