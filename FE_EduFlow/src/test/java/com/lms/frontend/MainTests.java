package com.lms.frontend;

import com.lms.frontend.service.CategoryService;
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
                .andExpect(content().string(containsString("/api/otp/verify-otp")));
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class TestConfig {

        @Bean
        @Primary
        CategoryService categoryService() {
            return (currentPage, size) -> null;
        }
    }

}
