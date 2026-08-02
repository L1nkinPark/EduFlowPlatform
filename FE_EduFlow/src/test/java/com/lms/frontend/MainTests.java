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
