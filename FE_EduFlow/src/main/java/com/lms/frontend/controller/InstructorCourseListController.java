package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.service.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/instructor/courses")
public class InstructorCourseListController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/list")
    public String showInstructorCourse(Model model, @RequestParam(required = false, defaultValue = "1") Integer currentPage,
                           @RequestParam(required = false, defaultValue = "5") Integer size, HttpSession session) {
//        AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");

        ApiResponse<List<CourseResponse>> apiResponse = courseService.getAllCourses(currentPage, size);

        List<CourseResponse> courseList = null;
        Map<String, Object> metadata = null;

        if (apiResponse == null) {
            courseList = new ArrayList<>();
            metadata = new HashMap<>();
        } else {
            courseList = apiResponse.getPayload();
            metadata = apiResponse.getMetadata();
        }
        model.addAttribute("courseList", courseList);

        model.addAttribute("totalPages", metadata.get("totalPages"));
        model.addAttribute("totalElements", metadata.get("totalElements"));
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("size", size);

        return "instructormng-course-list";
    }

    //Đơi phân quyền để test
//    @GetMapping("/courses/list")
//    public String showCourse(Model model,
//                             @RequestParam(required = false, defaultValue = "1") Integer currentPage,
//                             @RequestParam(required = false, defaultValue = "5") Integer size,
//                             HttpSession session) {
//
//        // Retrieve the user's accountId from the session
//        AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
//        Long accountId = userLogin != null ? userLogin.getAccountId() : null;
//
//        // If accountId is not available, handle it (e.g., redirect to login or show an error)
//        if (accountId == null) {
//            return "redirect:/login"; // or an error page if appropriate
//        }
//
//        // Fetch courses by the specific accountId
//        ApiResponse<List<CourseResponse>> apiResponse = courseService.getCoursesByAccount(accountId);
//
//        List<CourseResponse> courseList = null;
//        Map<String, Object> metadata = null;
//
//        if (apiResponse == null) {
//            courseList = new ArrayList<>();
//            metadata = new HashMap<>();
//        } else {
//            courseList = apiResponse.getPayload();
//            metadata = apiResponse.getMetadata();
//        }
//
//        model.addAttribute("courseList", courseList);
//        model.addAttribute("totalPages", metadata.get("totalPages"));
//        model.addAttribute("totalElements", metadata.get("totalElements"));
//        model.addAttribute("currentPage", currentPage);
//        model.addAttribute("size", size);
//
//        return "instructormng-course-list";
//    }


//    public String showInstructorCourseMng(Model model){
//        ApiResponse<List<CourseResponse>> apiResponse = courseService.getAllCourses(1, 9);
//        model.addAttribute("courses", apiResponse.getPayload());
//        return "instructormng-course-list";
//    }
}
