package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.CourseService;
import com.lms.frontend.service.OrderService;
import com.lms.frontend.service.LessonProgressService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/course")
public class CoursesDetailController {

    @Autowired
    CourseService courseService;

    @Autowired
    OrderService orderService;

    @Autowired
    LessonProgressService lessonProgressService;

    @Autowired
    MessageSource messageSource;

    @GetMapping(value = "/detail")
    public String showDetailCourse(Model model,
                                   @RequestParam(required = false) String courseId,
                                   @RequestParam(required = false) String error,
                                   HttpSession session) {
        if (courseId == null || courseId.isBlank()) {
            return "redirect:/course/all";
        }

        ApiResponse<CourseResponse> apiResponse = courseService.getCourseById(courseId);

        if (apiResponse == null || apiResponse.getPayload() == null) {
            return "redirect:/course/all";
        }

        model.addAttribute("courses", apiResponse.getPayload());

        boolean isPurchased = false;
        List<Long> completedLessonIds = new ArrayList<>();
        AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
        if (userLogin != null) {
            isPurchased = orderService.hasPurchased(courseId);
            if (isPurchased) {
                ApiResponse<List<Long>> progressResponse = lessonProgressService.getCompletedLessonIds(courseId);
                if (progressResponse != null && progressResponse.getPayload() != null) {
                    completedLessonIds = progressResponse.getPayload();
                }
            }
        }
        model.addAttribute("isPurchased", isPurchased);
        model.addAttribute("completedLessonIds", completedLessonIds);
        int totalLessons = apiResponse.getPayload().getChapters() == null ? 0
                : apiResponse.getPayload().getChapters().stream()
                .filter(chapter -> chapter.getLessons() != null)
                .mapToInt(chapter -> chapter.getLessons().size())
                .sum();
        int progressPercentage = totalLessons == 0 ? 0
                : (int) Math.round(completedLessonIds.size() * 100.0 / totalLessons);
        model.addAttribute("progressPercentage", progressPercentage);

        // Các khóa học khác để gợi ý (loại trừ khóa học hiện tại) — dùng cho mục
        // "Courses you might be interested in", thay cho việc tái sử dụng nhầm
        // biến course đơn lẻ như một list.
        List<CourseResponse> relatedCourses = new ArrayList<>();
        ApiResponse<List<CourseResponse>> relatedResponse = courseService.getAllCourses(1, 5, null);
        if (relatedResponse != null && relatedResponse.getPayload() != null) {
            for (CourseResponse c : relatedResponse.getPayload()) {
                if (!c.getCourseId().equals(courseId)) {
                    relatedCourses.add(c);
                }
                if (relatedCourses.size() >= 4) {
                    break;
                }
            }
        }
        model.addAttribute("relatedCourses", relatedCourses);
        String errorMessageKey = switch (error == null ? "" : error) {
            case "payment_unavailable" -> "detail.payment_unavailable";
            case "payment_failed" -> "detail.payment_failed";
            case "course_not_purchased" -> "detail.course_not_purchased";
            case "checkout_failed" -> "detail.checkout_failed";
            default -> null;
        };
        if (errorMessageKey != null) {
            model.addAttribute("checkoutError", messageSource.getMessage(
                    errorMessageKey, null, LocaleContextHolder.getLocale()));
        }

        return "courses-detail";
    }

}
