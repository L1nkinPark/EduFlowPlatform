package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.model.response.ChapterResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.model.response.LessonResponse;
import com.lms.frontend.service.CourseService;
import com.lms.frontend.service.LessonProgressService;
import com.lms.frontend.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseLearningController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private LessonProgressService lessonProgressService;

    @GetMapping("/learn")
    public String showLearningRoom(@RequestParam String courseId,
                                   @RequestParam(required = false) Long lessonId,
                                   HttpSession session,
                                   Model model) {
        AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
        if (userLogin == null) {
            return "redirect:/signin";
        }
        if (!orderService.hasPurchased(courseId)) {
            return "redirect:/course/detail?courseId=" + courseId + "&error=course_not_purchased";
        }

        ApiResponse<CourseResponse> courseResponse = courseService.getCourseById(courseId);
        if (courseResponse == null || courseResponse.getPayload() == null) {
            return "redirect:/course/all";
        }

        CourseResponse course = courseResponse.getPayload();
        List<LessonResponse> lessons = flattenLessons(course.getChapters());
        List<Long> completedLessonIds = getCompletedLessonIds(courseId);

        LessonResponse currentLesson = selectLesson(lessons, completedLessonIds, lessonId);
        int currentIndex = currentLesson == null ? -1 : lessons.indexOf(currentLesson);
        int progressPercentage = lessons.isEmpty() ? 0
                : (int) Math.round(completedLessonIds.size() * 100.0 / lessons.size());

        model.addAttribute("course", course);
        model.addAttribute("lessons", lessons);
        model.addAttribute("currentLesson", currentLesson);
        model.addAttribute("currentVideoUrl",
                currentLesson == null ? null : normalizeVideoUrl(currentLesson.getVideo()));
        model.addAttribute("currentVideoIsFile",
                currentLesson != null && isVideoFile(currentLesson.getVideo()));
        model.addAttribute("currentIndex", currentIndex);
        model.addAttribute("previousLesson", currentIndex > 0 ? lessons.get(currentIndex - 1) : null);
        model.addAttribute("nextLesson",
                currentIndex >= 0 && currentIndex < lessons.size() - 1 ? lessons.get(currentIndex + 1) : null);
        model.addAttribute("completedLessonIds", completedLessonIds);
        model.addAttribute("progressPercentage", progressPercentage);
        model.addAttribute("completedCount", completedLessonIds.size());
        model.addAttribute("totalLessons", lessons.size());
        return "course-learning";
    }

    private boolean isVideoFile(String videoUrl) {
        if (videoUrl == null) {
            return false;
        }
        String cleanUrl = videoUrl.toLowerCase().split("\\?")[0];
        return cleanUrl.endsWith(".mp4") || cleanUrl.endsWith(".webm") || cleanUrl.endsWith(".ogg");
    }

    private String normalizeVideoUrl(String videoUrl) {
        if (videoUrl == null || videoUrl.isBlank() || isVideoFile(videoUrl)) {
            return videoUrl;
        }
        if (videoUrl.contains("youtube.com/embed/")) {
            return videoUrl;
        }
        String videoId = null;
        int watchIndex = videoUrl.indexOf("youtube.com/watch?v=");
        if (watchIndex >= 0) {
            videoId = videoUrl.substring(watchIndex + "youtube.com/watch?v=".length());
        } else {
            int shortIndex = videoUrl.indexOf("youtu.be/");
            if (shortIndex >= 0) {
                videoId = videoUrl.substring(shortIndex + "youtu.be/".length());
            }
        }
        if (videoId != null) {
            int separator = videoId.indexOf('&');
            if (separator >= 0) {
                videoId = videoId.substring(0, separator);
            }
            separator = videoId.indexOf('?');
            if (separator >= 0) {
                videoId = videoId.substring(0, separator);
            }
            return "https://www.youtube-nocookie.com/embed/" + videoId;
        }
        return videoUrl;
    }

    private List<LessonResponse> flattenLessons(List<ChapterResponse> chapters) {
        if (chapters == null) {
            return Collections.emptyList();
        }
        List<LessonResponse> lessons = new ArrayList<>();
        for (ChapterResponse chapter : chapters) {
            if (chapter.getLessons() != null) {
                lessons.addAll(chapter.getLessons());
            }
        }
        return lessons;
    }

    private List<Long> getCompletedLessonIds(String courseId) {
        ApiResponse<List<Long>> response = lessonProgressService.getCompletedLessonIds(courseId);
        return response != null && response.getPayload() != null
                ? response.getPayload()
                : new ArrayList<>();
    }

    private LessonResponse selectLesson(List<LessonResponse> lessons,
                                        List<Long> completedLessonIds,
                                        Long requestedLessonId) {
        if (requestedLessonId != null) {
            for (LessonResponse lesson : lessons) {
                if (requestedLessonId.equals(lesson.getLessonId())) {
                    return lesson;
                }
            }
        }
        for (LessonResponse lesson : lessons) {
            if (!completedLessonIds.contains(lesson.getLessonId())) {
                return lesson;
            }
        }
        return lessons.isEmpty() ? null : lessons.get(0);
    }
}
