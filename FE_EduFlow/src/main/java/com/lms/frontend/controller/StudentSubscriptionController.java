package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentSubscriptionController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/subscription")
    public String showSubscriptionPage(Model model, HttpSession session){
        AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
        if (userLogin != null) {
            ApiResponse<List<CourseResponse>> apiResponse = orderService.getUserCourses();
            if (apiResponse != null && apiResponse.getPayload() != null) {
                model.addAttribute("purchasedCourses", apiResponse.getPayload());
            } else {
                model.addAttribute("purchasedCourses", Collections.emptyList());
            }
        } else {
            model.addAttribute("purchasedCourses", Collections.emptyList());
        }
        return "student-subscriptions";
    }
}
