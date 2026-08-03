package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InstructorCourseListControllerTest {

    private InstructorCourseListController controller;
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        controller = new InstructorCourseListController();
        courseService = mock(CourseService.class);
        ResourceBundleMessageSource messages = new ResourceBundleMessageSource();
        messages.setBasename("messages");
        messages.setDefaultEncoding("UTF-8");
        ReflectionTestUtils.setField(controller, "courseService", courseService);
        ReflectionTestUtils.setField(controller, "messageSource", messages);
    }

    @Test
    void failedSaveStaysOnFormWithVisibleError() {
        CourseResponse course = new CourseResponse();
        course.setCourseName("Course under test");
        when(courseService.saveCourse(course)).thenReturn(null);
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.processAddCourse(course, model);

        assertEquals("instructormng-course-add", view);
        assertEquals(course, model.get("course"));
        assertEquals("The course could not be saved. Check the information and try again.", model.get("error"));
    }

    @Test
    void successfulSaveRedirectsWithConfirmationFlag() {
        CourseResponse course = new CourseResponse();
        ApiResponse<CourseResponse> response = new ApiResponse<>();
        response.ok(course);
        when(courseService.saveCourse(course)).thenReturn(response);

        String view = controller.processAddCourse(course, new ExtendedModelMap());

        assertEquals("redirect:/instructor/mycourse?saved=true", view);
    }
}
