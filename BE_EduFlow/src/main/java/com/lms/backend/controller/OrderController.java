package com.lms.backend.controller;

import com.lms.backend.exception.InvalidPromoCodeException;
import com.lms.backend.model.entity.Account;
import com.lms.backend.model.entity.Course;
import com.lms.backend.model.entity.Order;
import com.lms.backend.model.mapper.CourseMapper;
import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.repository.CourseRepository;
import com.lms.backend.repository.AccountRepository;
import com.lms.backend.security.CustomUserDetails;
import com.lms.backend.service.OrderService;
import com.lms.backend.service.PromoCodeService;
import com.lms.backend.util.VnPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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
        public String promoCode;
        public long expectedAmount;
        public long timestamp;

        public PendingPayment(String courseId, long accountId, String promoCode, long expectedAmount) {
            this.courseId = courseId;
            this.accountId = accountId;
            this.promoCode = promoCode;
            this.expectedAmount = expectedAmount;
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

    @Autowired
    private PromoCodeService promoCodeService;

    @Autowired
    private VnPayUtil vnPayUtil;

    @PostMapping("/promo/validate")
    public ResponseEntity<ApiResponse> validatePromoCode(@RequestParam String courseId,
                                                          @RequestParam String promoCode) {
        ApiResponse response = new ApiResponse();
        try {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found: " + courseId));

            double originalAmount = course.getPrice();
            double finalAmount = promoCodeService.calculateDiscountedAmount(promoCode, originalAmount);

            Map<String, Object> data = new HashMap<>();
            data.put("originalAmount", originalAmount);
            data.put("finalAmount", finalAmount);
            data.put("discountAmount", originalAmount - finalAmount);

            response.ok("Áp dụng mã giảm giá thành công", data);
            return ResponseEntity.ok(response);
        } catch (InvalidPromoCodeException ex) {
            response.error(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception ex) {
            response.error(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/vnpay-url")
    public ResponseEntity<ApiResponse> getVnPayUrl(@RequestParam String courseId,
                                                   @RequestParam String redirectOrigin,
                                                   @RequestParam(required = false) String promoCode,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        ApiResponse response = new ApiResponse();
        if (userDetails == null) {
            response.error("User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            Account account = userDetails.getAccount();
            if (orderService.hasPurchasedCourse(account, courseId)) {
                response.error("Course has already been purchased");
                return ResponseEntity.badRequest().body(response);
            }
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found: " + courseId));

            // Giá khóa học (course.price) được lưu bằng VND, khớp với đơn vị
            // dùng trong PromoCode (minOrderAmount/discountAmount/maxDiscountAmount)
            // và với các trang quản lý của instructor/admin.
            double amount = course.getPrice();
            if (promoCode != null && !promoCode.isBlank()) {
                amount = promoCodeService.calculateDiscountedAmount(promoCode, amount);
            }
            // VNPay yêu cầu số tiền phải >= 5,000 VND theo quy định sandbox.
            if (amount < 5000) {
                amount = 5000;
            }
            // VNPay expects the amount multiplied by 100 (smallest currency unit).
            long vnpAmount = Math.round(amount * 100);

            // Clean up old pending payments
            long now = System.currentTimeMillis();
            pendingPayments.entrySet().removeIf(entry -> (now - entry.getValue().timestamp) > 1800000); // 30 mins

            String txnRef = "EP" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
            pendingPayments.put(txnRef,
                    new PendingPayment(courseId, account.getAccountId(), promoCode, vnpAmount));

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", vnPayUtil.getTmnCode());
            vnp_Params.put("vnp_Amount", String.valueOf(vnpAmount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", txnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan khoa hoc EduFlow");
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", redirectOrigin + "/course/vnpay-callback");
            vnp_Params.put("vnp_IpAddr", "127.0.0.1");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            String vnp_CreateDate = formatter.format(new Date());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            String secureHash = vnPayUtil.hashAllFields(vnp_Params);

            // Build final query string
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder query = new StringBuilder();
            for (String fieldName : fieldNames) {
                String fieldValue = vnp_Params.get(fieldName);
                if (fieldValue != null && fieldValue.length() > 0) {
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()));
                    query.append("=");
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                    query.append("&");
                }
            }
            String queryUrl = vnPayUtil.getPaymentUrl() + "?" + query + "vnp_SecureHash=" + secureHash;
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

            String calculatedHash = vnPayUtil.hashAllFields(hashParams);
            if (vnp_SecureHash == null || !MessageDigest.isEqual(
                    calculatedHash.getBytes(StandardCharsets.UTF_8),
                    vnp_SecureHash.getBytes(StandardCharsets.UTF_8))) {
                response.error("Invalid secure hash signature");
                return ResponseEntity.badRequest().body(response);
            }

            String responseCode = params.get("vnp_ResponseCode");
            String transactionStatus = params.get("vnp_TransactionStatus");
            String txnRef = params.get("vnp_TxnRef");

            if ("00".equals(responseCode) && "00".equals(transactionStatus)) {
                PendingPayment pending = pendingPayments.get(txnRef);
                if (pending == null) {
                    response.error("Transaction mapping not found or expired");
                    return ResponseEntity.badRequest().body(response);
                }
                long paidAmount;
                try {
                    paidAmount = Long.parseLong(params.getOrDefault("vnp_Amount", ""));
                } catch (NumberFormatException ex) {
                    response.error("Invalid payment amount");
                    return ResponseEntity.badRequest().body(response);
                }
                if (paidAmount != pending.expectedAmount) {
                    response.error("Payment amount does not match the order");
                    return ResponseEntity.badRequest().body(response);
                }

                Account user = accountRepository.findById(pending.accountId)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                Order order = orderService.createOrder(user, pending.courseId);

                if (pending.promoCode != null && !pending.promoCode.isBlank()) {
                    promoCodeService.markCodeAsUsed(pending.promoCode);
                }
                pendingPayments.remove(txnRef, pending);

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

    // Lịch sử đơn hàng thật của học viên đang đăng nhập (dùng cho trang Invoice).
    @GetMapping("/history")
    public ResponseEntity<ApiResponse> getOrderHistory(@AuthenticationPrincipal CustomUserDetails userDetails) {
        ApiResponse response = new ApiResponse();
        if (userDetails == null) {
            response.error("User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            Account account = userDetails.getAccount();
            List<Order> orders = orderService.getOrdersByUser(account);

            List<com.lms.backend.model.response.OrderHistoryResponse> history = new ArrayList<>();
            for (Order order : orders) {
                com.lms.backend.model.response.OrderHistoryResponse dto = new com.lms.backend.model.response.OrderHistoryResponse();
                dto.setOrderId(order.getId());
                dto.setTotalAmount(order.getTotalAmount());
                dto.setOrderDate(order.getOrderDate());
                if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                    List<String> names = new ArrayList<>();
                    for (var item : order.getOrderItems()) {
                        if (item.getCourse() != null) {
                            names.add(item.getCourse().getCourseName());
                        }
                    }
                    dto.setCourseNames(names);
                }
                history.add(dto);
            }
            // Newest first.
            history.sort((a, b) -> {
                if (a.getOrderDate() == null || b.getOrderDate() == null) return 0;
                return b.getOrderDate().compareTo(a.getOrderDate());
            });

            response.ok("OK", history);
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
