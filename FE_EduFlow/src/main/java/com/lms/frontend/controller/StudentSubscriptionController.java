package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.OrderService;
import com.lms.frontend.service.LessonProgressService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student")
public class StudentSubscriptionController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private LessonProgressService lessonProgressService;

    @GetMapping("/subscription")
    public String showSubscriptionPage(Model model, HttpSession session){
        Map<String, Integer> courseProgressMap = new HashMap<>();
        AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
        if (userLogin != null) {
            ApiResponse<List<CourseResponse>> apiResponse = orderService.getUserCourses();
            if (apiResponse != null && apiResponse.getPayload() != null) {
                List<CourseResponse> purchased = apiResponse.getPayload();
                model.addAttribute("purchasedCourses", purchased);
                for (CourseResponse course : purchased) {
                    ApiResponse<Integer> progressResponse = lessonProgressService.getCourseProgressPercentage(course.getCourseId());
                    if (progressResponse != null && progressResponse.getPayload() != null) {
                        courseProgressMap.put(course.getCourseId(), progressResponse.getPayload());
                    } else {
                        courseProgressMap.put(course.getCourseId(), 0);
                    }
                }
            } else {
                model.addAttribute("purchasedCourses", Collections.emptyList());
            }
        } else {
            model.addAttribute("purchasedCourses", Collections.emptyList());
        }
        model.addAttribute("courseProgressMap", courseProgressMap);
        return "student-subscriptions";
    }
}
