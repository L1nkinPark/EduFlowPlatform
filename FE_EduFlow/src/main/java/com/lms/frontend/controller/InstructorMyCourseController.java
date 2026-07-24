package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/instructor")
public class InstructorMyCourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/mycourse")
    public String showMyCoursePage(Model model) {
        ApiResponse<List<CourseResponse>> apiResponse = courseService.getMyCourses();
        List<CourseResponse> courses = (apiResponse != null && apiResponse.getPayload() != null)
                ? apiResponse.getPayload()
                : Collections.emptyList();

        model.addAttribute("courseList", courses);
        return "instructor-my-courses";
    }

}
