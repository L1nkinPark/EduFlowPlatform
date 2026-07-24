package com.lms.backend.controller;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.entity.Course;
import com.lms.backend.model.entity.OrderItem;
import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.model.response.InstructorDashboardResponse;
import com.lms.backend.model.response.InstructorOrderResponse;
import com.lms.backend.model.response.InstructorStudentResponse;
import com.lms.backend.repository.CourseRepository;
import com.lms.backend.repository.OrderItemRepository;
import com.lms.backend.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * REST endpoints used exclusively by the Instructor dashboard: real earnings,
 * orders (sales), and student data — all scoped strictly to the courses owned
 * by the currently authenticated instructor. No other instructor's data is
 * ever exposed.
 */
@RestController
@RequestMapping("/api/instructor")
@PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
public class InstructorController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    private Account requireInstructor(CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("Authentication required.");
        }
        return userDetails.getAccount();
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse> getDashboard(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Account instructor = requireInstructor(userDetails);
        ApiResponse response = new ApiResponse();

        List<Course> courses = courseRepository.findByAccount(instructor);
        List<Account> students = orderItemRepository.findDistinctStudentsByInstructor(instructor);

        InstructorDashboardResponse dashboard = new InstructorDashboardResponse();
        dashboard.setTotalCourses(courses.size());
        dashboard.setTotalStudents(students.size());
        dashboard.setTotalSales(orderItemRepository.countSalesByInstructor(instructor));
        dashboard.setTotalRevenue(orderItemRepository.sumRevenueByInstructor(instructor));

        response.ok("OK", dashboard);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse> getOrders(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Account instructor = requireInstructor(userDetails);
        ApiResponse response = new ApiResponse();

        List<OrderItem> orderItems = orderItemRepository.findByInstructor(instructor);
        List<InstructorOrderResponse> orders = orderItems.stream().map(item -> {
            InstructorOrderResponse dto = new InstructorOrderResponse();
            dto.setOrderId(item.getOrder().getId());
            dto.setCourseId(item.getCourse().getCourseId());
            dto.setCourseName(item.getCourse().getCourseName());
            Account student = item.getOrder().getUser();
            dto.setStudentName(student != null ? student.getFullName() : "N/A");
            dto.setStudentEmail(student != null ? student.getEmail() : "N/A");
            dto.setPrice(item.getPrice());
            dto.setOrderDate(item.getOrder().getOrderDate());
            return dto;
        }).collect(Collectors.toList());

        response.ok("OK", orders);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/students")
    public ResponseEntity<ApiResponse> getStudents(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Account instructor = requireInstructor(userDetails);
        ApiResponse response = new ApiResponse();

        List<OrderItem> orderItems = orderItemRepository.findByInstructor(instructor);

        // Group course names by student to build the enrolled-courses list per student.
        Map<Long, InstructorStudentResponse> byStudent = new LinkedHashMap<>();
        for (OrderItem item : orderItems) {
            Account student = item.getOrder().getUser();
            if (student == null) continue;

            InstructorStudentResponse dto = byStudent.computeIfAbsent(student.getAccountId(), id -> {
                InstructorStudentResponse s = new InstructorStudentResponse();
                s.setAccountId(student.getAccountId());
                s.setFullName(student.getFullName());
                s.setEmail(student.getEmail());
                s.setEnrolledCourseNames(new ArrayList<>());
                return s;
            });
            dto.getEnrolledCourseNames().add(item.getCourse().getCourseName());
        }

        response.ok("OK", new ArrayList<>(byStudent.values()));
        return ResponseEntity.ok(response);
    }
}
