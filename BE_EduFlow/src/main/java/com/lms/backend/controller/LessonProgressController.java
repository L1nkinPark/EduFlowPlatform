package com.lms.backend.controller;

import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.security.CustomUserDetails;
import com.lms.backend.service.LessonProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
public class LessonProgressController {

    @Autowired
    private LessonProgressService lessonProgressService;

    @PostMapping("/toggle")
    public ResponseEntity<ApiResponse> toggleProgress(@RequestParam Long lessonId,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        ApiResponse response = new ApiResponse();
        if (userDetails == null) {
            response.error("User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            boolean completed = lessonProgressService.toggleLessonProgress(userDetails.getAccount(), lessonId);
            response.ok("Progress toggled successfully", completed);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.error(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse> getCompletedLessons(@PathVariable String courseId,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        ApiResponse response = new ApiResponse();
        if (userDetails == null) {
            response.error("User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            List<Long> completedIds = lessonProgressService.getCompletedLessonIds(userDetails.getAccount(), courseId);
            response.ok("OK", completedIds);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.error(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/course/{courseId}/percentage")
    public ResponseEntity<ApiResponse> getPercentage(@PathVariable String courseId,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        ApiResponse response = new ApiResponse();
        if (userDetails == null) {
            response.error("User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            int percentage = lessonProgressService.getCourseProgressPercentage(userDetails.getAccount(), courseId);
            response.ok("OK", percentage);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.error(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
