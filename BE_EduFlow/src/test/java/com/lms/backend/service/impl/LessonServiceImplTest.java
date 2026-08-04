package com.lms.backend.service.impl;

import com.lms.backend.model.entity.Chapter;
import com.lms.backend.model.entity.Lesson;
import com.lms.backend.model.request.LessonRequest;
import com.lms.backend.repository.ChapterRepository;
import com.lms.backend.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LessonServiceImplTest {

    private LessonServiceImpl service;
    private LessonRepository lessonRepository;
    private ChapterRepository chapterRepository;

    @BeforeEach
    void setUp() {
        service = new LessonServiceImpl();
        lessonRepository = mock(LessonRepository.class);
        chapterRepository = mock(ChapterRepository.class);
        ReflectionTestUtils.setField(service, "lessonRepository", lessonRepository);
        ReflectionTestUtils.setField(service, "chapterRepository", chapterRepository);
    }

    @Test
    void createsUploadedDocumentLessonWithMetadata() {
        Chapter chapter = new Chapter();
        LessonRequest request = new LessonRequest();
        request.setChapterId(9L);
        request.setTitle("  Tài liệu buổi 1  ");
        request.setLessonType("document");
        request.setDocumentUrl("https://cdn.example.com/lesson.pdf");
        request.setDocumentName("lesson.pdf");
        request.setDocumentContentType("application/pdf");
        request.setDuration(12);

        when(chapterRepository.findById(9L)).thenReturn(Optional.of(chapter));
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Lesson saved = service.createLesson(request);

        assertEquals("Tài liệu buổi 1", saved.getTitle());
        assertEquals("DOCUMENT", saved.getLessonType());
        assertEquals("https://cdn.example.com/lesson.pdf", saved.getDocumentUrl());
        assertEquals("lesson.pdf", saved.getDocumentName());
        assertEquals("application/pdf", saved.getDocumentContentType());
        assertEquals(chapter, saved.getChapter());
    }

    @Test
    void rejectsLessonWithoutChapterBeforeRepositoryAccess() {
        LessonRequest request = new LessonRequest();
        request.setTitle("Bài học");
        request.setVideo("https://example.com/video.mp4");

        assertThrows(IllegalArgumentException.class, () -> service.createLesson(request));

        verify(chapterRepository, never()).findById(any());
        verify(lessonRepository, never()).save(any());
    }

    @Test
    void rejectsEmptyDocumentLesson() {
        LessonRequest request = new LessonRequest();
        request.setChapterId(1L);
        request.setTitle("Bài đọc");
        request.setLessonType("DOCUMENT");

        assertThrows(IllegalArgumentException.class, () -> service.createLesson(request));

        verify(chapterRepository, never()).findById(any());
    }
}
