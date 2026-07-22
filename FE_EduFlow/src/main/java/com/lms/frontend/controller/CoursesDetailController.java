package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.CourseService;
import com.lms.frontend.service.OrderService;
import com.lms.frontend.service.LessonProgressService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/course")
public class CoursesDetailController {

    @Autowired
    CourseService courseService;

    @Autowired
    OrderService orderService;

    @Autowired
    LessonProgressService lessonProgressService;

    @GetMapping(value = "/detail")
    public String showDetailCourse(Model model, @RequestParam(required = false) String courseId, HttpSession session) {
        if (courseId == null || courseId.isBlank()) {
            return "redirect:/course/all";
        }

        ApiResponse<CourseResponse> apiResponse = courseService.getCourseById(courseId);

        if (apiResponse == null || apiResponse.getPayload() == null) {
            return "redirect:/course/all";
        }

        model.addAttribute("courses", apiResponse.getPayload());

        boolean isPurchased = false;
        List<Long> completedLessonIds = new ArrayList<>();
        AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
        if (userLogin != null) {
            isPurchased = orderService.hasPurchased(courseId);
            if (isPurchased) {
                ApiResponse<List<Long>> progressResponse = lessonProgressService.getCompletedLessonIds(courseId);
                if (progressResponse != null && progressResponse.getPayload() != null) {
                    completedLessonIds = progressResponse.getPayload();
                }
            }
        }
        model.addAttribute("isPurchased", isPurchased);
        model.addAttribute("completedLessonIds", completedLessonIds);

        return "courses-detail";
    }

}
