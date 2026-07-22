package com.lms.backend.controller;

import com.lms.backend.model.entity.Lesson;
import com.lms.backend.model.request.LessonRequest;
import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.model.response.LessonResponse;
import com.lms.backend.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @PostMapping
    public ResponseEntity<ApiResponse> createLesson(@RequestBody LessonRequest request) {
        ApiResponse response = new ApiResponse();
        try {
            Lesson lesson = lessonService.createLesson(request);

            LessonResponse lessonResponse = new LessonResponse();
            lessonResponse.setLessonId(lesson.getLessonId());
            lessonResponse.setTitle(lesson.getTitle());
            lessonResponse.setLessonType(lesson.getLessonType());
            lessonResponse.setVideo(lesson.getVideo());
            lessonResponse.setContent(lesson.getContent());
            lessonResponse.setDuration(lesson.getDuration());
            lessonResponse.setStatus(lesson.isStatus());

            response.ok("Lesson created successfully", lessonResponse);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.error(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
