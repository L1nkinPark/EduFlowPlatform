package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.CourseService;
import com.lms.frontend.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/course")
public class CoursesDetailController {

    @Autowired
    CourseService courseService;

    @Autowired
    OrderService orderService;

    @GetMapping(value = "/detail")
    public String showDetailCourse(Model model, @RequestParam String courseId, HttpSession session) {
        ApiResponse<CourseResponse> apiResponse = courseService.getCourseById(courseId);

        model.addAttribute("courses", apiResponse.getPayload());

        boolean isPurchased = false;
        AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
        if (userLogin != null) {
            isPurchased = orderService.hasPurchased(courseId);
        }
        model.addAttribute("isPurchased", isPurchased);

        return "courses-detail";
    }

}
