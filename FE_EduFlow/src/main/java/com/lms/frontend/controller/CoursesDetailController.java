package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/course")
public class CoursesDetailController {

    @Autowired
    CourseService courseService;

    @GetMapping(value = "/detail")
    public String showDetailCourse(Model model, @RequestParam String courseId) {
        ApiResponse<CourseResponse> apiResponse = courseService.getCourseById(courseId);

//        if (apiResponse == null || apiResponse.getPayload() == null) {
//            // Trả về trang lỗi nếu không nhận được phản hồi từ API hoặc payload là null
//            return "error"; // Tên của trang lỗi (bạn cần tạo trang này)
//        }

        model.addAttribute("courses", apiResponse.getPayload());
        return "courses-detail"; // Tên của trang chi tiết khóa học
    }

}
