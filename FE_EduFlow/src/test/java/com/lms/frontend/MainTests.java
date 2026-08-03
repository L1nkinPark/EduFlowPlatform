package com.lms.frontend;

import com.lms.frontend.service.CategoryService;
import com.lms.frontend.service.CourseService;
import com.lms.frontend.service.PublicStatsService;
import com.lms.frontend.service.AccountService;
import com.lms.frontend.model.request.LoginRequest;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Import(MainTests.TestConfig.class)
class MainTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseService courseService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private Environment environment;

    @Test
    void contextLoads() {
        org.junit.jupiter.api.Assertions.assertEquals(
                "cookie",
                environment.getProperty("server.servlet.session.tracking-modes"));
    }

    @Test
    void signInPageRendersSuccessfully() throws Exception {
        mockMvc.perform(get("/signin"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"))
                .andExpect(model().attribute("currentUri", "/signin"))
                .andExpect(model().attributeExists("user"))
                .andExpect(content().string(containsString("action=\"/signin\"")))
                .andExpect(content().string(containsString("Email hoặc tên đăng nhập")))
                .andExpect(content().string(containsString("min-vh-100 d-flex flex-column")))
                .andExpect(content().string(not(containsString("Ghi nhớ đăng nhập"))))
                .andExpect(content().string(containsString("Đăng nhập vào</span> <span class=\"text-primary\">EduFlow")));
    }

    @Test
    void failedSignInShowsLocalizedVisibleError() throws Exception {
        ApiResponse<AuthResponse> response = new ApiResponse<>();
        response.error("invalid credentials");
        when(accountService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/signin")
                        .param("username", "legacy@example.com")
                        .param("password", "wrong-password")
                        .param("lang", "vi"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"))
                .andExpect(content().string(containsString("data-testid=\"login-error\"")))
                .andExpect(content().string(containsString("Email, tên đăng nhập hoặc mật khẩu không chính xác.")));
    }

    @Test
    void signUpPageUsesSameOriginOtpApiInProduction() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attributeExists("userRegister"))
                .andExpect(content().string(containsString("window.location.origin")))
                .andExpect(content().string(containsString("/api/otp/send-otp-signup")))
                .andExpect(content().string(containsString("response.status === 409")))
                .andExpect(content().string(containsString("emailRegistered")))
                .andExpect(content().string(containsString("/api/otp/verify-otp")));
    }

    @Test
    void forgotPasswordUsesOneTimeTokenAndVietnameseTranslations() throws Exception {
        mockMvc.perform(get("/forgot").param("lang", "vi"))
                .andExpect(status().isOk())
                .andExpect(view().name("forgot"))
                .andExpect(content().string(containsString("Đặt lại mật khẩu")))
                .andExpect(content().string(containsString("window.location.origin")))
                .andExpect(content().string(containsString("/api/password/send-otp")))
                .andExpect(content().string(containsString("/api/password/verify-otp")))
                .andExpect(content().string(not(containsString("/api/otp/validate-email"))))
                .andExpect(content().string(containsString("otpToken: passwordResetToken")));
    }

    @Test
    void forgotPasswordRendersEnglishTranslations() throws Exception {
        mockMvc.perform(get("/forgot").param("lang", "en"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Recover your account securely")));
    }

    @Test
    void forbiddenErrorPageIsLocalized() throws Exception {
        mockMvc.perform(get("/error")
                        .requestAttr(jakarta.servlet.RequestDispatcher.ERROR_STATUS_CODE, 403)
                        .param("lang", "en"))
                .andExpect(status().isForbidden())
                .andExpect(view().name("error"))
                .andExpect(content().string(containsString("You do not have permission to access this page.")))
                .andExpect(content().string(containsString("Back to home")));
    }

    @Test
    void homePageUsesVietnameseTitleAndSearchPlaceholder() throws Exception {
        mockMvc.perform(get("/").param("lang", "vi"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>EduFlow - Nền tảng học trực tuyến</title>")))
                .andExpect(content().string(containsString("placeholder=\"Bạn cần tìm khóa học nào?\"")))
                .andExpect(content().string(containsString("aria-label=\"Tìm kiếm\"")))
                .andExpect(content().string(not(containsString("alt=\"#\""))));
    }

    @Test
    void aboutPageIsTranslatedAndContainsNoPlaceholderCopy() throws Exception {
        mockMvc.perform(get("/about").param("lang", "vi"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("EduFlow là gì?")))
                .andExpect(content().string(containsString("Tầm nhìn của chúng tôi")))
                .andExpect(content().string(containsString("Đội ngũ sản phẩm")))
                .andExpect(content().string(not(containsString("Lorem Ipsum"))))
                .andExpect(content().string(not(containsString("Ibbie Eckart"))))
                .andExpect(content().string(not(containsString("alt=\"...\""))));
    }

    @Test
    void contactPageUsesMobileSafeGridAndLocalizedValidation() throws Exception {
        mockMvc.perform(get("/contact").param("lang", "vi"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("gx-3 gx-md-5")))
                .andExpect(content().string(containsString("Liên hệ với chúng tôi")));
    }

    @Test
    void faqPageRendersVietnameseQuestionsWithoutTemplateCopy() throws Exception {
        mockMvc.perform(get("/faq").param("lang", "vi"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Câu hỏi thường gặp")))
                .andExpect(content().string(containsString("Làm thế nào để đăng ký một khóa học?")))
                .andExpect(content().string(not(containsString("Anim pariatur"))));
    }

    @Test
    void careersPageUsesLocalizedRealContactFlow() throws Exception {
        mockMvc.perform(get("/career").param("lang", "vi"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Cùng EduFlow xây dựng tương lai giáo dục")))
                .andExpect(content().string(containsString("Nhà thiết kế UI/UX")))
                .andExpect(content().string(containsString("href=\"/contact\"")))
                .andExpect(content().string(not(containsString("Want to join Team Edutree"))))
                .andExpect(content().string(not(containsString("type=\"number\""))));
    }

    @Test
    void privacyPageRendersVietnamesePolicyContent() throws Exception {
        mockMvc.perform(get("/privacy/policy").param("lang", "vi"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Chính sách bảo mật")))
                .andExpect(content().string(containsString("Thông tin chúng tôi thu thập")))
                .andExpect(content().string(not(containsString("Information we collect"))));
    }

    @Test
    void termsPageRendersVietnameseTermsContent() throws Exception {
        mockMvc.perform(get("/term-condition").param("lang", "vi"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Điều khoản và điều kiện")))
                .andExpect(content().string(containsString("Tài khoản")))
                .andExpect(content().string(not(containsString("Intellectual Property Rights"))));
    }

    @Test
    void secondaryPagesAlsoRenderEnglish() throws Exception {
        mockMvc.perform(get("/faq").param("lang", "en"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("How do I enroll in a course?")));
        mockMvc.perform(get("/career").param("lang", "en"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("UI/UX Designer")));
        mockMvc.perform(get("/privacy/policy").param("lang", "en"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Information we collect")));
        mockMvc.perform(get("/term-condition").param("lang", "en"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Intellectual property")));
    }

    @Test
    void sharedLayoutAndCourseBreadcrumbUseEduFlowBrandAndLocale() throws Exception {
        mockMvc.perform(get("/course/all").param("lang", "vi"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("EduFlow")))
                .andExpect(content().string(containsString("Trang chủ")))
                .andExpect(content().string(containsString("Khóa học")))
                .andExpect(content().string(not(containsString("Edutree"))))
                .andExpect(content().string(not(containsString("https://askbootstrap.com/ Courses"))));
    }

    @Test
    void courseDetailTemplateHasLocalizedRecommendationsAndNoDeadActions() throws Exception {
        String template = new ClassPathResource("templates/courses-detail.html").getContentAsString(java.nio.charset.StandardCharsets.UTF_8);
        assertTrue(template.contains("th:text=\"#{detail.related}\""));
        assertTrue(template.contains("th:text=\"#{detail.instructor}\""));
        assertFalse(template.contains("Add to a list"));
        assertFalse(template.contains("See more Edutree course"));
        assertFalse(template.contains("askbootstrap.com"));
    }

    @Test
    void sharedStylesDoNotExposeLegacyHostOrFakeFontAssets() throws Exception {
        String styles = new ClassPathResource("static/css/style.css").getContentAsString(java.nio.charset.StandardCharsets.UTF_8);
        assertFalse(styles.contains("askbootstrap.com"));
        assertFalse(styles.contains("feather.eb9822a3be07c5110df9.html"));
        assertFalse(styles.contains("feather.f780c210ecac82116260.html"));
        assertTrue(styles.contains("var(--bs-breadcrumb-divider,\"/\")"));
    }

    @Test
    void courseDetailRendersLocalizedVietnameseContentAndTitle() throws Exception {
        CourseResponse course = new CourseResponse();
        course.setCourseId("course-test");
        course.setCourseName("Khóa học kiểm thử");
        course.setDescription("Mô tả kiểm thử");
        course.setChapters(java.util.Collections.emptyList());
        ApiResponse<CourseResponse> response = new ApiResponse<>();
        response.ok(course);
        when(courseService.getCourseById("course-test")).thenReturn(response);

        mockMvc.perform(get("/course/detail").param("courseId", "course-test").param("lang", "vi"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Khóa học kiểm thử - EduFlow</title>")))
                .andExpect(content().string(containsString("Các khóa học có thể bạn quan tâm")))
                .andExpect(content().string(containsString("Giảng viên")))
                .andExpect(content().string(not(containsString("Add to a list"))))
                .andExpect(content().string(not(containsString("alt=\"#\""))))
                .andExpect(content().string(not(containsString("askbootstrap.com"))));
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class TestConfig {

        @Bean
        @Primary
        CategoryService categoryService() {
            return (currentPage, size) -> null;
        }

        @Bean
        @Primary
        CourseService courseService() {
            return mock(CourseService.class);
        }

        @Bean
        @Primary
        PublicStatsService publicStatsService() {
            return () -> null;
        }

        @Bean
        @Primary
        AccountService accountService() {
            return mock(AccountService.class);
        }
    }

}
