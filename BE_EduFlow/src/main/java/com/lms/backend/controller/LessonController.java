package com.lms.backend.controller;

import com.lms.backend.exception.ForbiddenException;
import com.lms.backend.model.entity.Chapter;
import com.lms.backend.model.entity.Lesson;
import com.lms.backend.model.request.LessonRequest;
import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.model.response.LessonResponse;
import com.lms.backend.repository.ChapterRepository;
import com.lms.backend.security.CustomUserDetails;
import com.lms.backend.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private ChapterRepository chapterRepository;

    @PostMapping
    public ResponseEntity<ApiResponse> createLesson(@RequestBody LessonRequest request,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        ApiResponse response = new ApiResponse();
        try {
            assertOwnsChapter(request.getChapterId(), userDetails);

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
        } catch (ForbiddenException ex) {
            response.error(ex.getMessage());
            return ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN).body(response);
        } catch (Exception ex) {
            response.error(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Chỉ chủ khóa học (thông qua chapter) hoặc ADMIN mới được thêm lesson.
    private void assertOwnsChapter(Long chapterId, CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new ForbiddenException("Authentication required.");
        }
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new RuntimeException("Chapter not found: " + chapterId));

        boolean isAdmin = "ADMIN".equalsIgnoreCase(userDetails.getAccount().getRole());
        boolean isOwner = chapter.getCourse() != null
                && chapter.getCourse().getAccount() != null
                && chapter.getCourse().getAccount().getAccountId() == userDetails.getAccount().getAccountId();

        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("You do not own this chapter's course.");
        }
    }
}
