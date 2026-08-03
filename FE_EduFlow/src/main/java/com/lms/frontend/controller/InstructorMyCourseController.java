package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/instructor")
public class InstructorMyCourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/mycourse")
    public String showMyCoursePage(Model model,
                                   @RequestParam(name = "saved", required = false) Boolean saved) {
        ApiResponse<List<CourseResponse>> apiResponse = courseService.getMyCourses();
        List<CourseResponse> courses = (apiResponse != null && apiResponse.getPayload() != null)
                ? apiResponse.getPayload()
                : Collections.emptyList();

        model.addAttribute("courseList", courses);
        if (Boolean.TRUE.equals(saved)) {
            model.addAttribute("success", messageSource.getMessage(
                    "instructor.course_saved", null, LocaleContextHolder.getLocale()));
        }
        return "instructor-my-courses";
    }

}
