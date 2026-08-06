package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/course")
public class StudentOrderController {

    @Autowired
    private OrderService orderService;

    @Value("${payment.return-origin:}")
    private String configuredReturnOrigin;

    @PostMapping("/promo/validate")
    @ResponseBody
    public ApiResponse<Map<String, Object>> validatePromoCode(@RequestParam String courseId,
                                                                @RequestParam String promoCode,
                                                                HttpSession session) {
        AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
        if (userLogin == null) {
            ApiResponse<Map<String, Object>> response = new ApiResponse<>();
            response.error("Vui lòng đăng nhập để sử dụng mã giảm giá");
            return response;
        }

        ApiResponse<Map<String, Object>> apiResponse = orderService.validatePromoCode(courseId, promoCode);
        if (apiResponse == null) {
            ApiResponse<Map<String, Object>> response = new ApiResponse<>();
            response.error("Không thể kiểm tra mã giảm giá lúc này, vui lòng thử lại");
            return response;
        }
        return apiResponse;
    }

    @GetMapping("/checkout")
    public String checkoutCourse(@RequestParam String courseId,
                                  @RequestParam(required = false) String promoCode,
                                  HttpSession session, HttpServletRequest request) {
        AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
        if (userLogin == null) {
            return "redirect:/signin";
        }

        String origin = resolveReturnOrigin(request);

        ApiResponse<String> apiResponse = orderService.getVnPayUrl(courseId, origin, promoCode);
        if (apiResponse != null && "SUCCESS".equals(apiResponse.getStatus()) && apiResponse.getPayload() != null) {
            return "redirect:" + apiResponse.getPayload();
        }

        String errorCode = isPaymentConfigurationUnavailable(apiResponse)
                ? "payment_unavailable"
                : "checkout_failed";
        return "redirect:/course/detail?courseId=" + courseId + "&error=" + errorCode;
    }

    @GetMapping("/vnpay-callback")
    public String vnpayCallback(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (entry.getValue() != null && entry.getValue().length > 0) {
                params.put(entry.getKey(), entry.getValue()[0]);
            }
        }

        ApiResponse<String> apiResponse = orderService.verifyVnPayCallback(params);
        if (apiResponse != null && "SUCCESS".equals(apiResponse.getStatus())) {
            return "redirect:/course/success";
        }

        String courseId = (apiResponse != null && apiResponse.getPayload() != null) ? apiResponse.getPayload() : "";
        if (!courseId.isEmpty()) {
            return "redirect:/course/detail?courseId=" + courseId + "&error=payment_failed";
        }
        return "redirect:/course/all?error=payment_failed";
    }

    private boolean isPaymentConfigurationUnavailable(ApiResponse<String> response) {
        if (response == null || response.getMessage() == null) {
            return false;
        }
        String message = response.getMessage();
        return message.contains("VNPAY_") && message.contains("not configured");
    }

    private String resolveReturnOrigin(HttpServletRequest request) {
        if (configuredReturnOrigin != null && !configuredReturnOrigin.isBlank()) {
            return configuredReturnOrigin.replaceAll("/+$", "");
        }

        String origin = request.getScheme() + "://" + request.getServerName();
        if (request.getServerPort() != 80 && request.getServerPort() != 443) {
            origin += ":" + request.getServerPort();
        }
        return origin;
    }
}
