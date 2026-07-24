package com.lms.backend.controller;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.mapper.AccountMapper;
import com.lms.backend.model.request.RegisterRequest;
import com.lms.backend.model.response.AccountResponse;
import com.lms.backend.model.response.AdminDashboardResponse;
import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.model.response.AuthResponse;
import com.lms.backend.repository.AccountRepository;
import com.lms.backend.repository.CourseRepository;
import com.lms.backend.repository.OrderRepository;
import com.lms.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints used exclusively by the Admin dashboard: system-wide
 * monitoring stats and instructor/admin account management. Every endpoint
 * here requires the caller to be authenticated as ADMIN.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AuthService authService;

    // Tổng quan giám sát hệ thống: số lượng student/instructor/admin, tổng số khóa học,
    // tổng số order và tổng doanh thu.
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse> getDashboard() {
        ApiResponse response = new ApiResponse();

        AdminDashboardResponse dashboard = new AdminDashboardResponse();
        dashboard.setTotalStudents(accountRepository.countByRole("STUDENT"));
        dashboard.setTotalInstructors(accountRepository.countByRole("INSTRUCTOR"));
        dashboard.setTotalAdmins(accountRepository.countByRole("ADMIN"));
        dashboard.setTotalCourses(courseRepository.count());
        dashboard.setTotalOrders(orderRepository.count());
        dashboard.setTotalRevenue(orderRepository.sumTotalRevenue());

        response.ok("OK", dashboard);
        return ResponseEntity.ok(response);
    }

    // Danh sách account theo role (INSTRUCTOR, STUDENT, ADMIN) để admin giám sát.
    @GetMapping("/accounts")
    public ResponseEntity<ApiResponse> getAccountsByRole(@RequestParam(defaultValue = "INSTRUCTOR") String role) {
        ApiResponse response = new ApiResponse();

        List<Account> accounts = accountRepository.findByRoleOrderByAccountIdDesc(role.toUpperCase());
        List<AccountResponse> accountResponses = accountMapper.convertToDTO(accounts);

        response.ok("OK", accountResponses);
        return ResponseEntity.ok(response);
    }

    // Admin tạo account INSTRUCTOR mới. Việc kiểm tra quyền ADMIN được thực hiện 2 lần:
    // ở đây (method security) và trong AuthServiceImpl.register (kiểm tra SecurityContext),
    // để đảm bảo API /api/auth/register cũng không thể bị lợi dụng nếu gọi trực tiếp.
    @PostMapping("/instructors")
    public ResponseEntity<ApiResponse> createInstructor(@RequestBody RegisterRequest request) {
        ApiResponse response = new ApiResponse();

        request.setRole("INSTRUCTOR");
        AuthResponse authResponse = authService.register(request);

        response.ok("Instructor account created successfully", authResponse);
        return ResponseEntity.ok(response);
    }

    // Khóa / mở khóa account (ví dụ để đình chỉ 1 instructor vi phạm).
    @PutMapping("/accounts/{accountId}/status")
    public ResponseEntity<ApiResponse> updateAccountStatus(@PathVariable long accountId,
                                                            @RequestParam boolean status) {
        ApiResponse response = new ApiResponse();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));
        account.setStatus(status);
        accountRepository.save(account);

        response.ok("Account status updated", accountMapper.convertToDTO(account));
        return ResponseEntity.ok(response);
    }
}
