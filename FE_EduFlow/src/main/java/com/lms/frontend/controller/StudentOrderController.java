package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/course")
public class StudentOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/checkout")
    public String checkoutCourse(@RequestParam String courseId, HttpSession session, HttpServletRequest request) {
        AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
        if (userLogin == null) {
            return "redirect:/signin";
        }

        String origin = request.getScheme() + "://" + request.getServerName();
        if (request.getServerPort() != 80 && request.getServerPort() != 443) {
            origin += ":" + request.getServerPort();
        }

        ApiResponse<String> apiResponse = orderService.getVnPayUrl(courseId, origin);
        if (apiResponse != null && "SUCCESS".equals(apiResponse.getStatus()) && apiResponse.getPayload() != null) {
            return "redirect:" + apiResponse.getPayload();
        }

        return "redirect:/course/detail?courseId=" + courseId + "&error=checkout_failed";
    }

    @GetMapping("/vnpay-callback")
    public String vnpayCallback(HttpServletRequest request, HttpSession session) {
        AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
        if (userLogin == null) {
            return "redirect:/signin";
        }

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
}
