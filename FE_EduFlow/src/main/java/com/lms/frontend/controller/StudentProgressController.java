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
        return lessonProgressService.toggleProgress(lessonId);
    }
}
