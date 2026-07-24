package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/instructor/courses")
public class InstructorCourseListController {
    @Autowired
    private CourseService courseService;

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

    @PostMapping("/add")
    public String processAddCourse(CourseResponse course) {
        courseService.saveCourse(course);
        return "redirect:/instructor/mycourse";
    }

    // Alias for the old /instructor/courses/list route so any stale bookmarks/links
    // land on the real "My Courses" page (which is properly scoped to the
    // logged-in instructor's own courses) instead of a blank/broken page.
    @GetMapping("/list")
    public String redirectToMyCourses() {
        return "redirect:/instructor/mycourse";
    }
}
