package com.lms.frontend.controller;

import com.lms.frontend.model.request.LessonRequest;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.ChapterResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.model.response.LessonResponse;
import com.lms.frontend.service.ChapterService;
import com.lms.frontend.service.CourseService;
import com.lms.frontend.service.LessonService;
import com.lms.frontend.service.MediaStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InstructorCourseContentControllerTest {

    private InstructorCourseContentController controller;
    private CourseService courseService;
    private LessonService lessonService;
    private MediaStorageService mediaStorageService;

    @BeforeEach
    void setUp() {
        controller = new InstructorCourseContentController();
        courseService = mock(CourseService.class);
        lessonService = mock(LessonService.class);
        mediaStorageService = mock(MediaStorageService.class);
        ReflectionTestUtils.setField(controller, "courseService", courseService);
        ReflectionTestUtils.setField(controller, "chapterService", mock(ChapterService.class));
        ReflectionTestUtils.setField(controller, "lessonService", lessonService);
        ReflectionTestUtils.setField(controller, "mediaStorageService", mediaStorageService);
    }

    @Test
    void uploadsDocumentAndCreatesLessonInSelectedCourseChapter() {
        CourseResponse course = courseWithChapter(21L);
        ApiResponse<CourseResponse> courseResponse = new ApiResponse<>();
        courseResponse.ok(course);
        when(courseService.getCourseById("course-1")).thenReturn(courseResponse);
        when(mediaStorageService.uploadLessonDocument(any())).thenReturn(
                new MediaStorageService.UploadedMedia(
                        "https://cdn.example.com/bai-hoc.pdf", "bai-hoc.pdf", "application/pdf"));
        ApiResponse<LessonResponse> lessonResponse = new ApiResponse<>();
        lessonResponse.ok(new LessonResponse());
        when(lessonService.createLesson(any())).thenReturn(lessonResponse);

        LessonRequest request = new LessonRequest();
        request.setChapterId(21L);
        request.setTitle("Bài học PDF");
        request.setLessonType("DOCUMENT");
        MockMultipartFile file = new MockMultipartFile(
                "lessonFile", "bai-hoc.pdf", "application/pdf", "%PDF-test".getBytes());
        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

        String view = controller.createLesson("course-1", request, file, redirect);

        assertEquals("redirect:/instructor/courses/content?courseId=course-1", view);
        assertEquals("https://cdn.example.com/bai-hoc.pdf", request.getDocumentUrl());
        assertEquals("bai-hoc.pdf", request.getDocumentName());
        assertEquals("application/pdf", request.getDocumentContentType());
        verify(lessonService).createLesson(request);
    }

    @Test
    void rejectsChapterThatDoesNotBelongToCurrentCourse() {
        ApiResponse<CourseResponse> courseResponse = new ApiResponse<>();
        courseResponse.ok(courseWithChapter(21L));
        when(courseService.getCourseById("course-1")).thenReturn(courseResponse);
        LessonRequest request = new LessonRequest();
        request.setChapterId(999L);
        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

        controller.createLesson("course-1", request, null, redirect);

        assertEquals("Vui lòng chọn một chương thuộc khóa học này.", redirect.getFlashAttributes().get("error"));
        assertEquals(request, redirect.getFlashAttributes().get("newLesson"));
        verify(mediaStorageService, never()).uploadLessonDocument(any());
        verify(lessonService, never()).createLesson(any());
    }

    @Test
    void keepsEnteredLessonDataWhenBackendRejectsCreation() {
        ApiResponse<CourseResponse> courseResponse = new ApiResponse<>();
        courseResponse.ok(courseWithChapter(21L));
        when(courseService.getCourseById("course-1")).thenReturn(courseResponse);
        ApiResponse<LessonResponse> lessonResponse = new ApiResponse<>();
        lessonResponse.error("Đường dẫn video không hợp lệ.");
        when(lessonService.createLesson(any())).thenReturn(lessonResponse);
        LessonRequest request = new LessonRequest();
        request.setChapterId(21L);
        request.setTitle("Bài đang nhập");
        request.setLessonType("VIDEO");
        request.setVideo("not-a-url");
        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

        controller.createLesson("course-1", request, null, redirect);

        assertEquals("Đường dẫn video không hợp lệ.", redirect.getFlashAttributes().get("error"));
        assertEquals(request, redirect.getFlashAttributes().get("newLesson"));
    }

    private CourseResponse courseWithChapter(Long chapterId) {
        ChapterResponse chapter = new ChapterResponse();
        chapter.setChapterId(chapterId);
        CourseResponse course = new CourseResponse();
        course.setCourseId("course-1");
        course.setChapters(List.of(chapter));
        return course;
    }
}
