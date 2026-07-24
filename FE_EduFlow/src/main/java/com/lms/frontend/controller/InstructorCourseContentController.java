package com.lms.frontend.controller;

import com.lms.frontend.model.request.ChapterRequest;
import com.lms.frontend.model.request.LessonRequest;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.service.ChapterService;
import com.lms.frontend.service.CourseService;
import com.lms.frontend.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/instructor/courses/content")
public class InstructorCourseContentController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private LessonService lessonService;

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
    public String createChapter(@RequestParam String courseId, ChapterRequest newChapter, Model model) {
        newChapter.setCourseId(courseId);
        ApiResponse<?> apiResponse = chapterService.createChapter(newChapter);

        if (apiResponse == null || !"SUCCESS".equals(apiResponse.getStatus())) {
            model.addAttribute("error", apiResponse != null ? apiResponse.getMessage() : "Tạo chương thất bại.");
        } else {
            model.addAttribute("success", "Đã tạo chương: " + newChapter.getTitle());
        }

        return showContentPage(courseId, model);
    }

    @PostMapping("/lessons")
    public String createLesson(@RequestParam String courseId, LessonRequest newLesson, Model model) {
        ApiResponse<?> apiResponse = lessonService.createLesson(newLesson);

        if (apiResponse == null || !"SUCCESS".equals(apiResponse.getStatus())) {
            model.addAttribute("error", apiResponse != null ? apiResponse.getMessage() : "Tạo bài học thất bại.");
        } else {
            model.addAttribute("success", "Đã tạo bài học: " + newLesson.getTitle());
        }

        return showContentPage(courseId, model);
    }
}
