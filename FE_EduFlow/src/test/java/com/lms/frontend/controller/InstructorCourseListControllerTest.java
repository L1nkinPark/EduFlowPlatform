package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.service.CourseService;
import com.lms.frontend.service.MediaStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InstructorCourseListControllerTest {

    private InstructorCourseListController controller;
    private CourseService courseService;
    private MediaStorageService mediaStorageService;

    @BeforeEach
    void setUp() {
        controller = new InstructorCourseListController();
        courseService = mock(CourseService.class);
        mediaStorageService = mock(MediaStorageService.class);
        ResourceBundleMessageSource messages = new ResourceBundleMessageSource();
        messages.setBasename("messages");
        messages.setDefaultEncoding("UTF-8");
        ReflectionTestUtils.setField(controller, "courseService", courseService);
        ReflectionTestUtils.setField(controller, "mediaStorageService", mediaStorageService);
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

    @Test
    void externalCoverUrlIsUsedForImageAndThumbnail() {
        CourseResponse course = new CourseResponse();
        course.setImage("  https://images.example.com/course.jpg  ");
        ApiResponse<CourseResponse> response = new ApiResponse<>();
        response.ok(course);
        when(courseService.saveCourse(course)).thenReturn(response);

        String view = controller.processAddCourse(course, null, new ExtendedModelMap());

        assertEquals("redirect:/instructor/mycourse?saved=true", view);
        assertEquals("https://images.example.com/course.jpg", course.getImage());
        assertEquals(course.getImage(), course.getThumbnail());
    }

    @Test
    void insecureCoverUrlShowsUsefulErrorWithoutSaving() {
        CourseResponse course = new CourseResponse();
        course.setImage("http://images.example.com/course.jpg");
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.processAddCourse(course, null, model);

        assertEquals("instructormng-course-add", view);
        assertEquals("URL ảnh phải là liên kết HTTPS trực tiếp (ví dụ: https://.../anh.jpg).", model.get("error"));
        verify(courseService, never()).saveCourse(any());
    }

    @Test
    void uploadedCoverReplacesUrlAndIsUsedEverywhere() {
        CourseResponse course = new CourseResponse();
        course.setImage("https://images.example.com/old.jpg");
        MockMultipartFile file = new MockMultipartFile(
                "coverFile", "new.png", "image/png", new byte[]{1, 2, 3});
        when(mediaStorageService.uploadCourseImage(file)).thenReturn(
                new MediaStorageService.UploadedMedia("https://cdn.example.com/new.png", "new.png", "image/png"));
        ApiResponse<CourseResponse> response = new ApiResponse<>();
        response.ok(course);
        when(courseService.saveCourse(course)).thenReturn(response);

        controller.processAddCourse(course, file, new ExtendedModelMap());

        assertEquals("https://cdn.example.com/new.png", course.getImage());
        assertEquals(course.getImage(), course.getThumbnail());
    }
}
