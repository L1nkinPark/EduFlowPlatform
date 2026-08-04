package com.lms.frontend.controller;

import com.lms.frontend.model.request.ChapterRequest;
import com.lms.frontend.model.request.LessonRequest;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.service.ChapterService;
import com.lms.frontend.service.CourseService;
import com.lms.frontend.service.LessonService;
import com.lms.frontend.service.MediaStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/instructor/courses/content")
public class InstructorCourseContentController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private MediaStorageService mediaStorageService;

    @GetMapping
    public String showContentPage(@RequestParam String courseId, Model model) {
        ApiResponse<CourseResponse> apiResponse = courseService.getCourseById(courseId);
        CourseResponse course = (apiResponse != null) ? apiResponse.getPayload() : null;

        model.addAttribute("course", course);
        model.addAttribute("courseId", courseId);

        if (!model.containsAttribute("newChapter")) {
            ChapterRequest chapterRequest = new ChapterRequest();
            chapterRequest.setCourseId(courseId);
            model.addAttribute("newChapter", chapterRequest);
        }
        if (!model.containsAttribute("newLesson")) {
            model.addAttribute("newLesson", new LessonRequest());
        }

        return "instructor-course-content";
    }

    @PostMapping("/chapters")
    public String createChapter(@RequestParam String courseId,
                                @ModelAttribute("newChapter") ChapterRequest newChapter,
                                RedirectAttributes redirectAttributes) {
        newChapter.setCourseId(courseId);
        ApiResponse<?> apiResponse = chapterService.createChapter(newChapter);

        if (apiResponse == null || !"SUCCESS".equals(apiResponse.getStatus())) {
            redirectAttributes.addFlashAttribute("error",
                    apiResponse != null ? apiResponse.getMessage() : "Tạo chương thất bại.");
        } else {
            redirectAttributes.addFlashAttribute("success", "Đã tạo chương: " + newChapter.getTitle());
        }

        return "redirect:/instructor/courses/content?courseId=" + courseId;
    }

    @PostMapping(value = "/lessons", consumes = "multipart/form-data")
    public String createLesson(@RequestParam String courseId,
                               @ModelAttribute("newLesson") LessonRequest newLesson,
                               @RequestParam(name = "lessonFile", required = false) MultipartFile lessonFile,
                               RedirectAttributes redirectAttributes) {
        ApiResponse<CourseResponse> courseResponse = courseService.getCourseById(courseId);
        CourseResponse course = courseResponse == null ? null : courseResponse.getPayload();
        if (course == null || course.getChapters() == null || newLesson.getChapterId() == null
                || course.getChapters().stream().noneMatch(
                chapter -> newLesson.getChapterId().equals(chapter.getChapterId()))) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn một chương thuộc khóa học này.");
            redirectAttributes.addFlashAttribute("newLesson", newLesson);
            return "redirect:/instructor/courses/content?courseId=" + courseId;
        }

        try {
            if (lessonFile != null && !lessonFile.isEmpty()) {
                if (!"DOCUMENT".equalsIgnoreCase(newLesson.getLessonType())) {
                    throw new IllegalArgumentException("Chỉ bài học tài liệu mới nhận tệp PDF, Word, PowerPoint hoặc Excel.");
                }
                MediaStorageService.UploadedMedia uploaded = mediaStorageService.uploadLessonDocument(lessonFile);
                newLesson.setDocumentUrl(uploaded.url());
                newLesson.setDocumentName(uploaded.originalName());
                newLesson.setDocumentContentType(uploaded.contentType());
            }
        } catch (IllegalArgumentException | IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            redirectAttributes.addFlashAttribute("newLesson", newLesson);
            return "redirect:/instructor/courses/content?courseId=" + courseId;
        }

        ApiResponse<?> apiResponse = lessonService.createLesson(newLesson);

        if (apiResponse == null || !"SUCCESS".equals(apiResponse.getStatus())) {
            redirectAttributes.addFlashAttribute("error",
                    apiResponse != null ? apiResponse.getMessage() : "Tạo bài học thất bại.");
            redirectAttributes.addFlashAttribute("newLesson", newLesson);
        } else {
            redirectAttributes.addFlashAttribute("success", "Đã tạo bài học: " + newLesson.getTitle());
        }

        return "redirect:/instructor/courses/content?courseId=" + courseId;
    }
}
