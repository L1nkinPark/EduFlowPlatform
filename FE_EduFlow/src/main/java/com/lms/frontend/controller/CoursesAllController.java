package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping("/course")
public class CoursesAllController {
//
    @Autowired
    CourseService courseService;

    @GetMapping("/all")
    public String showAllCourse(Model model,
                                @RequestParam(required = false) String keyword,
                                @RequestParam(required = false) Long categoryId,
                                @RequestParam(required = false) Long subCategoryId) {
        ApiResponse<List<CourseResponse>> apiResponse =
                courseService.getAllCourses(1, 60, keyword, categoryId, subCategoryId);

        if (apiResponse == null || apiResponse.getPayload() == null) {
            model.addAttribute("courses", Collections.emptyList());
        } else {
            model.addAttribute("courses", apiResponse.getPayload());
        }
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedSubCategoryId", subCategoryId);

        return "courses";
    }

}
