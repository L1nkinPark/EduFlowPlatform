package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class HomeController {

    @Autowired
    CourseService courseService;

    @GetMapping("")
    public String showHomePage(Model model) {
        ApiResponse<List<CourseResponse>> apiResponse = courseService.getAllCourses(1, 8);

        if (apiResponse == null || apiResponse.getPayload() == null) {
            model.addAttribute("courses", Collections.emptyList()); // Hoặc xử lý lỗi tùy ý
        } else {
            model.addAttribute("courses", apiResponse.getPayload());
        }

        return "index";
    }

    @GetMapping("/logout")
    public String logout(jakarta.servlet.http.HttpSession session) {
        session.invalidate();
        return "redirect:/signin";
    }
}
