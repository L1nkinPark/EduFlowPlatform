package com.lms.frontend;

import com.lms.frontend.service.CategoryService;
import com.lms.frontend.service.CourseService;
import com.lms.frontend.service.PublicStatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    void contextLoads() {
    }

    @Test
    void signInPageRendersSuccessfully() throws Exception {
        mockMvc.perform(get("/signin"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"))
                .andExpect(model().attribute("currentUri", "/signin"))
                .andExpect(model().attributeExists("user"))
                .andExpect(content().string(containsString("action=\"/signin\"")));
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
    void homePageUsesVietnameseTitleAndSearchPlaceholder() throws Exception {
        mockMvc.perform(get("/").param("lang", "vi"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>EduFlow - Nền tảng học trực tuyến</title>")))
                .andExpect(content().string(containsString("placeholder=\"Bạn cần tìm khóa học nào?\"")));
    }

    @Test
    void aboutPageIsTranslatedAndContainsNoPlaceholderCopy() throws Exception {
        mockMvc.perform(get("/about").param("lang", "vi"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("EduFlow là gì?")))
                .andExpect(content().string(containsString("Tầm nhìn của chúng tôi")))
                .andExpect(content().string(not(containsString("Lorem Ipsum"))));
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
    }

}
