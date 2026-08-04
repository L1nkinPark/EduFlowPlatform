package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.service.CourseService;
import com.lms.frontend.service.MediaStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@Controller
@RequestMapping("/instructor/courses")
public class InstructorCourseListController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private MediaStorageService mediaStorageService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/add")
    public String showAddCourseForm(Model model, @RequestParam(required = false) String courseId) {
        CourseResponse course = null;
        if (courseId != null && !courseId.trim().isEmpty()) {
            ApiResponse<CourseResponse> apiResponse = courseService.getCourseById(courseId);
            if (apiResponse != null) {
                course = apiResponse.getPayload();
            }
        }
        if (course == null) {
            course = new CourseResponse();
        }
        model.addAttribute("course", course);
        return "instructormng-course-add";
    }

    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public String processAddCourse(CourseResponse course,
                                   @RequestParam(name = "coverFile", required = false) MultipartFile coverFile,
                                   Model model) {
        try {
            String coverUrl;
            if (coverFile != null && !coverFile.isEmpty()) {
                coverUrl = mediaStorageService.uploadCourseImage(coverFile).url();
            } else {
                String submittedUrl = hasText(course.getImage()) ? course.getImage() : course.getThumbnail();
                coverUrl = normalizeImageLocation(submittedUrl);
            }
            if (!hasText(coverUrl)) {
                coverUrl = "/img/globel/b1.jpg";
            }
            // The application historically had separate image/thumbnail fields,
            // while different screens used a different one. Keep them in sync so
            // one cover selected by the instructor appears everywhere.
            course.setImage(coverUrl);
            course.setThumbnail(coverUrl);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            model.addAttribute("course", course);
            model.addAttribute("error", ex.getMessage());
            return "instructormng-course-add";
        }

        ApiResponse<CourseResponse> response = courseService.saveCourse(course);
        if (response == null || !"SUCCESS".equals(response.getStatus()) || response.getPayload() == null) {
            model.addAttribute("course", course);
            model.addAttribute("error", messageSource.getMessage(
                    "instructor.course_save_failed", null, LocaleContextHolder.getLocale()));
            return "instructormng-course-add";
        }
        return "redirect:/instructor/mycourse?saved=true";
    }

    // Kept for controller-level callers that do not submit a multipart file.
    public String processAddCourse(CourseResponse course, Model model) {
        return processAddCourse(course, null, model);
    }

    private String normalizeImageLocation(String value) {
        if (!hasText(value)) {
            return null;
        }
        String normalized = value.trim();
        if (normalized.startsWith("/")) {
            return normalized;
        }
        try {
            URI uri = URI.create(normalized);
            if (!"https".equalsIgnoreCase(uri.getScheme()) || uri.getHost() == null) {
                throw new IllegalArgumentException(
                        "URL ảnh phải là liên kết HTTPS trực tiếp (ví dụ: https://.../anh.jpg).");
            }
            return uri.toASCIIString();
        } catch (IllegalArgumentException ex) {
            if (ex.getMessage() != null && ex.getMessage().startsWith("URL ảnh")) {
                throw ex;
            }
            throw new IllegalArgumentException("URL ảnh không hợp lệ. Hãy dán liên kết HTTPS trực tiếp đến ảnh.");
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    // Alias for the old /instructor/courses/list route so any stale bookmarks/links
    // land on the real "My Courses" page (which is properly scoped to the
    // logged-in instructor's own courses) instead of a blank/broken page.
    @GetMapping("/list")
    public String redirectToMyCourses() {
        return "redirect:/instructor/mycourse";
    }
}
