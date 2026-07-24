package com.lms.backend.controller;

import com.lms.backend.exception.ForbiddenException;
import com.lms.backend.model.entity.Chapter;
import com.lms.backend.model.entity.Course;
import com.lms.backend.model.request.ChapterRequest;
import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.model.response.ChapterResponse;
import com.lms.backend.repository.CourseRepository;
import com.lms.backend.security.CustomUserDetails;
import com.lms.backend.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chapters")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping
    public ResponseEntity<ApiResponse> createChapter(@RequestBody ChapterRequest request,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        ApiResponse response = new ApiResponse();
        try {
            assertOwnsCourse(request.getCourseId(), userDetails);

            Chapter chapter = chapterService.createChapter(request);

            ChapterResponse chapterResponse = new ChapterResponse();
            chapterResponse.setChapterId(chapter.getChapterId());
            chapterResponse.setTitle(chapter.getTitle());
            chapterResponse.setDescription(chapter.getDescription());
            chapterResponse.setStatus(chapter.isStatus());

            response.ok("Chapter created successfully", chapterResponse);
            return ResponseEntity.ok(response);
        } catch (ForbiddenException ex) {
            response.error(ex.getMessage());
            return ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN).body(response);
        } catch (Exception ex) {
            response.error(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Chỉ chủ khóa học (instructor tạo ra course đó) hoặc ADMIN mới được thêm chapter.
    private void assertOwnsCourse(String courseId, CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new ForbiddenException("Authentication required.");
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found: " + courseId));

        boolean isAdmin = "ADMIN".equalsIgnoreCase(userDetails.getAccount().getRole());
        boolean isOwner = course.getAccount() != null
                && course.getAccount().getAccountId() == userDetails.getAccount().getAccountId();

        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("You do not own this course.");
        }
    }
}
