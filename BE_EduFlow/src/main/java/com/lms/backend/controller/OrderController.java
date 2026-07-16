package com.lms.backend.controller;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.entity.Course;
import com.lms.backend.model.entity.Order;
import com.lms.backend.model.mapper.CourseMapper;
import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.security.CustomUserDetails;
import com.lms.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CourseMapper courseMapper;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam String courseId,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        ApiResponse response = new ApiResponse();
        if (userDetails == null) {
            response.error("User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            Account account = userDetails.getAccount();
            Order order = orderService.createOrder(account, courseId);
            response.ok("Order created successfully", order.getId());
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.error(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/my-courses")
    public ResponseEntity<ApiResponse> getMyCourses(@AuthenticationPrincipal CustomUserDetails userDetails) {
        ApiResponse response = new ApiResponse();
        if (userDetails == null) {
            response.error("User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            Account account = userDetails.getAccount();
            List<Course> courses = orderService.getPurchasedCourses(account);
            response.ok("OK", courseMapper.convertToDTO(courses));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.error(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/check-purchase")
    public ResponseEntity<ApiResponse> checkPurchase(@RequestParam String courseId,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        ApiResponse response = new ApiResponse();
        if (userDetails == null) {
            response.ok("OK", false);
            return ResponseEntity.ok(response);
        }

        try {
            Account account = userDetails.getAccount();
            boolean hasPurchased = orderService.hasPurchasedCourse(account, courseId);
            response.ok("OK", hasPurchased);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.error(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
