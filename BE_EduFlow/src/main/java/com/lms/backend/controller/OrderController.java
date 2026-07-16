package com.lms.backend.controller;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.entity.Course;
import com.lms.backend.model.entity.Order;
import com.lms.backend.model.mapper.CourseMapper;
import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.repository.CourseRepository;
import com.lms.backend.repository.AccountRepository;
import com.lms.backend.security.CustomUserDetails;
import com.lms.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Map<String, PendingPayment> pendingPayments = new ConcurrentHashMap<>();

    public static class PendingPayment {
        public String courseId;
        public long accountId;
        public long timestamp;

        public PendingPayment(String courseId, long accountId) {
            this.courseId = courseId;
            this.accountId = accountId;
            this.timestamp = System.currentTimeMillis();
        }
    }

    @Autowired
    private OrderService orderService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AccountRepository accountRepository;

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

    @GetMapping("/vnpay-url")
    public ResponseEntity<ApiResponse> getVnPayUrl(@RequestParam String courseId,
                                                   @RequestParam String redirectOrigin,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        ApiResponse response = new ApiResponse();
        if (userDetails == null) {
            response.error("User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            Account account = userDetails.getAccount();
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found: " + courseId));

            // Clean up old pending payments
            long now = System.currentTimeMillis();
            pendingPayments.entrySet().removeIf(entry -> (now - entry.getValue().timestamp) > 1800000); // 30 mins

            String txnRef = "EP" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
            pendingPayments.put(txnRef, new PendingPayment(courseId, account.getAccountId()));

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", com.lms.backend.util.VnPayUtil.TMN_CODE);
            vnp_Params.put("vnp_Amount", "10000000"); // 100,000 VND for testing
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", txnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan khoa hoc: " + course.getCourseName());
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", redirectOrigin + "/course/vnpay-callback");
            vnp_Params.put("vnp_IpAddr", "127.0.0.1");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            String vnp_CreateDate = formatter.format(new Date());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            String secureHash = com.lms.backend.util.VnPayUtil.hashAllFields(vnp_Params);

            // Build final query string
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder query = new StringBuilder();
            for (String fieldName : fieldNames) {
                String fieldValue = vnp_Params.get(fieldName);
                if (fieldValue != null && fieldValue.length() > 0) {
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()));
                    query.append("=");
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()).replace("+", "%20"));
                    query.append("&");
                }
            }
            String queryUrl = com.lms.backend.util.VnPayUtil.VNP_URL + "?" + query.toString() + "vnp_SecureHash=" + secureHash;
            response.ok("VNPAY URL generated", queryUrl);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.error(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/vnpay-callback")
    public ResponseEntity<ApiResponse> vnpayCallback(@RequestBody Map<String, String> params) {
        ApiResponse response = new ApiResponse();
        try {
            String vnp_SecureHash = params.get("vnp_SecureHash");
            Map<String, String> hashParams = new HashMap<>(params);
            hashParams.remove("vnp_SecureHash");
            hashParams.remove("vnp_SecureHashType");

            String calculatedHash = com.lms.backend.util.VnPayUtil.hashAllFields(hashParams);
            if (!calculatedHash.equals(vnp_SecureHash)) {
                response.error("Invalid secure hash signature");
                return ResponseEntity.badRequest().body(response);
            }

            String responseCode = params.get("vnp_ResponseCode");
            String txnRef = params.get("vnp_TxnRef");

            if ("00".equals(responseCode)) {
                PendingPayment pending = pendingPayments.remove(txnRef);
                if (pending == null) {
                    response.error("Transaction mapping not found or expired");
                    return ResponseEntity.badRequest().body(response);
                }

                Account user = accountRepository.findById(pending.accountId)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                Order order = orderService.createOrder(user, pending.courseId);
                response.ok("Payment successful and order created", pending.courseId);
                return ResponseEntity.ok(response);
            } else {
                response.error("Payment transaction failed with response code: " + responseCode);
                return ResponseEntity.badRequest().body(response);
            }
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
