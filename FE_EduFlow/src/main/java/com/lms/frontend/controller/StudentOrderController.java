package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/course")
public class StudentOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/checkout")
    public String checkoutCourse(@RequestParam String courseId, HttpSession session) {
        AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
        if (userLogin == null) {
            return "redirect:/signin";
        }
        
        ApiResponse<Void> apiResponse = orderService.createOrder(courseId);
        if (apiResponse != null && "SUCCESS".equals(apiResponse.getStatus())) {
            return "redirect:/course/success";
        }
        
        return "redirect:/course/detail?courseId=" + courseId + "&error=checkout_failed";
    }
}
