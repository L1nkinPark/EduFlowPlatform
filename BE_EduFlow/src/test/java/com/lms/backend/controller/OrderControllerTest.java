package com.lms.backend.controller;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.entity.Course;
import com.lms.backend.model.entity.Order;
import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.repository.AccountRepository;
import com.lms.backend.repository.CourseRepository;
import com.lms.backend.security.CustomUserDetails;
import com.lms.backend.service.OrderService;
import com.lms.backend.service.PromoCodeService;
import com.lms.backend.util.VnPayUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderControllerTest {

    private OrderController controller;
    private OrderService orderService;
    private CourseRepository courseRepository;
    private AccountRepository accountRepository;
    private PromoCodeService promoCodeService;
    private VnPayUtil vnPayUtil;
    private Account account;
    private Course course;

    @BeforeEach
    void setUp() {
        controller = new OrderController();
        orderService = mock(OrderService.class);
        courseRepository = mock(CourseRepository.class);
        accountRepository = mock(AccountRepository.class);
        promoCodeService = mock(PromoCodeService.class);
        vnPayUtil = new VnPayUtil();
        ReflectionTestUtils.setField(vnPayUtil, "tmnCode", "TESTCODE");
        ReflectionTestUtils.setField(vnPayUtil, "hashSecret", "test-hash-secret-for-vnpay");
        ReflectionTestUtils.setField(vnPayUtil, "paymentUrl",
                "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html");

        ReflectionTestUtils.setField(controller, "orderService", orderService);
        ReflectionTestUtils.setField(controller, "courseRepository", courseRepository);
        ReflectionTestUtils.setField(controller, "accountRepository", accountRepository);
        ReflectionTestUtils.setField(controller, "promoCodeService", promoCodeService);
        ReflectionTestUtils.setField(controller, "vnPayUtil", vnPayUtil);

        account = new Account();
        account.setAccountId(7L);
        account.setUsername("student");
        account.setRole("STUDENT");
        account.setStatus(true);

        course = new Course();
        course.setCourseId("animation-101");
        course.setCourseName("2D Animation");
        course.setPrice(490_000.0);

        when(orderService.hasPurchasedCourse(account, "animation-101"))
                .thenReturn(false);
        when(courseRepository.findById("animation-101"))
                .thenReturn(Optional.of(course));
    }

    @Test
    void sandboxUrlCarriesTheExactDisplayedCoursePriceAndRequiredFields() {
        ResponseEntity<ApiResponse> result = createPaymentUrl(null);

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        Map<String, String> params = queryParams((String) result.getBody().getPayload());

        assertEquals("49000000", params.get("vnp_Amount"));
        assertEquals("VND", params.get("vnp_CurrCode"));
        assertEquals("https://learn.example.test/course/vnpay-callback",
                params.get("vnp_ReturnUrl"));
        assertTrue(params.get("vnp_TxnRef").matches("EP[0-9a-f]{32}"));
        assertTrue(params.get("vnp_OrderInfo").matches("[A-Za-z0-9 ]+"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime created = LocalDateTime.parse(params.get("vnp_CreateDate"), formatter);
        LocalDateTime expires = LocalDateTime.parse(params.get("vnp_ExpireDate"), formatter);
        assertEquals(Duration.ofMinutes(15), Duration.between(created, expires));

        String secureHash = params.remove("vnp_SecureHash");
        assertEquals(vnPayUtil.hashAllFields(params), secureHash);
    }

    @Test
    void promoValidationAndVnPayUseTheSameRoundedAmount() {
        when(promoCodeService.calculateDiscountedAmount("SAVE10", 490_000.0))
                .thenReturn(441_000.0);

        ResponseEntity<ApiResponse> validation = controller.validatePromoCode(
                "animation-101", "SAVE10");
        Map<String, Object> validationPayload = (Map<String, Object>)
                validation.getBody().getPayload();
        assertEquals(441_000L, validationPayload.get("finalAmount"));

        ResponseEntity<ApiResponse> payment = createPaymentUrl("SAVE10");
        Map<String, String> params = queryParams(
                (String) payment.getBody().getPayload());
        assertEquals("44100000", params.get("vnp_Amount"));
    }

    @Test
    void successfulCallbackStoresTheExactDiscountedAmountPaidAtVnPay() {
        when(promoCodeService.calculateDiscountedAmount("SAVE10", 490_000.0))
                .thenReturn(441_000.0);
        Map<String, String> checkoutParams = queryParams(
                (String) createPaymentUrl("SAVE10").getBody().getPayload());

        Map<String, String> callback = new HashMap<>();
        callback.put("vnp_Amount", checkoutParams.get("vnp_Amount"));
        callback.put("vnp_ResponseCode", "00");
        callback.put("vnp_TransactionStatus", "00");
        callback.put("vnp_TmnCode", "TESTCODE");
        callback.put("vnp_TxnRef", checkoutParams.get("vnp_TxnRef"));
        callback.put("vnp_SecureHash", vnPayUtil.hashAllFields(callback));

        when(accountRepository.findById(7L)).thenReturn(Optional.of(account));
        when(orderService.createOrder(account, "animation-101", 441_000L))
                .thenReturn(new Order());

        ResponseEntity<ApiResponse> result = controller.vnpayCallback(callback);

        assertEquals(200, result.getStatusCode().value());
        verify(orderService).createOrder(account, "animation-101", 441_000L);
        verify(promoCodeService).markCodeAsUsed("SAVE10");
    }

    private ResponseEntity<ApiResponse> createPaymentUrl(String promoCode) {
        return controller.getVnPayUrl(
                "animation-101", "https://learn.example.test", promoCode,
                new CustomUserDetails(account));
    }

    private Map<String, String> queryParams(String url) {
        Map<String, String> params = new HashMap<>();
        String rawQuery = URI.create(url).getRawQuery();
        for (String pair : rawQuery.split("&")) {
            String[] parts = pair.split("=", 2);
            params.put(
                    URLDecoder.decode(parts[0], StandardCharsets.UTF_8),
                    URLDecoder.decode(parts.length > 1 ? parts[1] : "",
                            StandardCharsets.UTF_8));
        }
        return params;
    }
}
