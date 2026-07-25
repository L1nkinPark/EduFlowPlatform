package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.service.LessonProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentProgressController {

    @Autowired
    private LessonProgressService lessonProgressService;

    @PostMapping("/student/progress/toggle")
    public ApiResponse<Boolean> toggleProgress(@RequestParam Long lessonId) {
        ApiResponse<Boolean> response = lessonProgressService.toggleProgress(lessonId);
        if (response != null) {
            return response;
        }
        ApiResponse<Boolean> errorResponse = new ApiResponse<>();
        errorResponse.error("Không thể cập nhật tiến độ học tập lúc này");
        return errorResponse;
    }
}
