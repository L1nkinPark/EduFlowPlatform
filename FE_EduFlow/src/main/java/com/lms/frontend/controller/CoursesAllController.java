package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/course")
public class CoursesAllController {
//
    @Autowired
    CourseService courseService;

    @GetMapping("/all")
    public String showAllCourse(Model model) {
        ApiResponse<List<CourseResponse>> apiResponse = courseService.getAllCourses(1, 9);
        model.addAttribute("courses", apiResponse.getPayload());
        return "courses";
    }

}
