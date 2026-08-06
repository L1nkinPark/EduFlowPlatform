package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentOrderControllerTest {

    private StudentOrderController controller;
    private OrderService orderService;
    private MockHttpSession session;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        controller = new StudentOrderController();
        orderService = mock(OrderService.class);
        ReflectionTestUtils.setField(controller, "orderService", orderService);

        session = new MockHttpSession();
        session.setAttribute("userLogin", new AuthResponse());
        request = new MockHttpServletRequest();
        request.setScheme("http");
        request.setServerName("example.test");
        request.setServerPort(80);
    }

    @Test
    void missingVnPayConfigurationGetsActionableErrorCode() {
        ApiResponse<String> response = new ApiResponse<>();
        response.error("VNPAY_TMN_CODE is not configured");
        when(orderService.getVnPayUrl("course-1", "http://example.test", null)).thenReturn(response);

        String result = controller.checkoutCourse("course-1", null, session, request);

        assertEquals("redirect:/course/detail?courseId=course-1&error=payment_unavailable", result);
    }

    @Test
    void unknownCheckoutFailureUsesGenericErrorCode() {
        when(orderService.getVnPayUrl("course-1", "http://example.test", null)).thenReturn(null);

        String result = controller.checkoutCourse("course-1", null, session, request);

        assertEquals("redirect:/course/detail?courseId=course-1&error=checkout_failed", result);
    }

    @Test
    void checkoutUsesConfiguredPublicOriginForTheVnPayReturnUrl() {
        ReflectionTestUtils.setField(controller, "configuredReturnOrigin", "https://learn.example.test/");
        ApiResponse<String> response = new ApiResponse<>();
        response.ok("https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?token=test");
        when(orderService.getVnPayUrl(
                "course-1", "https://learn.example.test", null)).thenReturn(response);

        String result = controller.checkoutCourse("course-1", null, session, request);

        assertEquals("redirect:https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?token=test", result);
    }
}
