package com.lms.frontend.controller;

import com.lms.frontend.model.request.SignUpRequest;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.service.AdminService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BindingResult;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminInstructorControllerTest {

    private AdminInstructorController controller;
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        controller = new AdminInstructorController();
        adminService = mock(AdminService.class);
        ResourceBundleMessageSource messages = new ResourceBundleMessageSource();
        messages.setBasename("messages");
        messages.setDefaultEncoding("UTF-8");
        ReflectionTestUtils.setField(controller, "adminService", adminService);
        ReflectionTestUtils.setField(controller, "messageSource", messages);
        LocaleContextHolder.setLocale(Locale.forLanguageTag("vi"));
    }

    @AfterEach
    void resetLocale() {
        LocaleContextHolder.resetLocaleContext();
    }

    @Test
    void blankUsernameDefaultsToEmailAndShowsLocalizedSuccess() {
        SignUpRequest request = new SignUpRequest();
        request.setFullName("Giảng viên Test");
        request.setEmail("teacher@example.test");
        request.setUsername("  ");
        request.setPassword("safe-password");
        BindingResult binding = mock(BindingResult.class);
        when(binding.hasErrors()).thenReturn(false);
        ApiResponse<Object> created = new ApiResponse<>();
        created.ok(new Object());
        doReturn(created).when(adminService).createInstructor(request);
        when(adminService.getAccountsByRole("INSTRUCTOR")).thenReturn(null);
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.createInstructor(request, binding, model);

        assertEquals("admin-instructors", view);
        assertEquals("teacher@example.test", request.getUsername());
        assertEquals("Tạo tài khoản giảng viên thành công: teacher@example.test", model.get("success"));
        verify(adminService).createInstructor(request);
    }
}
