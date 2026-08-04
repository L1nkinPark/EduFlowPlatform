package com.lms.backend.service.impl;

import com.lms.backend.exception.DataNotFoundException;
import com.lms.backend.model.entity.Chapter;
import com.lms.backend.model.entity.Lesson;
import com.lms.backend.model.request.LessonRequest;
import com.lms.backend.repository.ChapterRepository;
import com.lms.backend.repository.LessonRepository;
import com.lms.backend.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Override
    public Lesson createLesson(LessonRequest request) {
        if (request.getChapterId() == null) {
            throw new IllegalArgumentException("Vui lòng chọn chương cho bài học.");
        }
        String title = request.getTitle() == null ? "" : request.getTitle().trim();
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập tên bài học.");
        }
        if (title.length() > 100) {
            throw new IllegalArgumentException("Tên bài học không được vượt quá 100 ký tự.");
        }
        if (request.getDuration() < 0) {
            throw new IllegalArgumentException("Thời lượng bài học không thể là số âm.");
        }

        String lessonType = request.getLessonType() == null
                ? "VIDEO"
                : request.getLessonType().trim().toUpperCase(java.util.Locale.ROOT);
        if (!"VIDEO".equals(lessonType) && !"DOCUMENT".equals(lessonType)) {
            throw new IllegalArgumentException("Loại bài học không hợp lệ.");
        }
        String video = trimToNull(request.getVideo());
        String content = trimToNull(request.getContent());
        String documentUrl = trimToNull(request.getDocumentUrl());
        if ("VIDEO".equals(lessonType) && video == null) {
            throw new IllegalArgumentException("Vui lòng nhập đường dẫn video.");
        }
        if ("DOCUMENT".equals(lessonType) && content == null && documentUrl == null) {
            throw new IllegalArgumentException("Vui lòng nhập nội dung hoặc tải lên một tài liệu.");
        }

        Chapter chapter = chapterRepository.findById(request.getChapterId())
                .orElseThrow(() -> new DataNotFoundException("Chapter not found: " + request.getChapterId()));

        Lesson lesson = new Lesson();
        lesson.setTitle(title);
        lesson.setLessonType(lessonType);
        lesson.setVideo(video);
        lesson.setContent(content);
        lesson.setDocumentUrl(documentUrl);
        lesson.setDocumentName(trimToNull(request.getDocumentName()));
        lesson.setDocumentContentType(trimToNull(request.getDocumentContentType()));
        lesson.setDuration(request.getDuration());
        lesson.setStatus(request.isStatus());
        lesson.setChapter(chapter);

        return lessonRepository.save(lesson);
    }

    private String trimToNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }
}
