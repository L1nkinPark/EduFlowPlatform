package com.lms.backend.controller;

import com.lms.backend.model.entity.Chapter;
import com.lms.backend.model.request.ChapterRequest;
import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.model.response.ChapterResponse;
import com.lms.backend.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chapters")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @PostMapping
    public ResponseEntity<ApiResponse> createChapter(@RequestBody ChapterRequest request) {
        ApiResponse response = new ApiResponse();
        try {
            Chapter chapter = chapterService.createChapter(request);

            ChapterResponse chapterResponse = new ChapterResponse();
            chapterResponse.setChapterId(chapter.getChapterId());
            chapterResponse.setTitle(chapter.getTitle());
            chapterResponse.setDescription(chapter.getDescription());
            chapterResponse.setStatus(chapter.isStatus());

            response.ok("Chapter created successfully", chapterResponse);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.error(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
