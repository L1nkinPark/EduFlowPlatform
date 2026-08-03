package com.lms.backend.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.MailSendException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new FailingController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void missingResourceReturns404InsteadOf500() throws Exception {
        mockMvc.perform(get("/test/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("ERROR"));
    }

    @Test
    void missingAuthenticationReturns401InsteadOf500() throws Exception {
        mockMvc.perform(get("/test/unauthorized"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deniedRoleReturnsGeneric403InsteadOfIncorrectAdminMessage() throws Exception {
        mockMvc.perform(get("/test/forbidden"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied: insufficient privileges."));
    }

    @Test
    void expectedBusinessConflictReturns409InsteadOf500() throws Exception {
        mockMvc.perform(get("/test/conflict"))
                .andExpect(status().isConflict());
    }

    @Test
    void unavailableEmailProviderReturns503InsteadOf500() throws Exception {
        mockMvc.perform(get("/test/mail"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message").value("Email service is temporarily unavailable. Please try again later."));
    }

    @Test
    void missingRequestParameterReturns400InsteadOf500() throws Exception {
        mockMvc.perform(get("/test/required"))
                .andExpect(status().isBadRequest());
    }

    @RestController
    static class FailingController {
        @GetMapping("/test/missing")
        void missing() {
            throw new ResourceNotFoundException("Missing");
        }

        @GetMapping("/test/unauthorized")
        void unauthorized() {
            throw new UnauthorizedException("Authentication required");
        }

        @GetMapping("/test/forbidden")
        void forbidden() {
            throw new org.springframework.security.access.AccessDeniedException("Denied");
        }

        @GetMapping("/test/conflict")
        void conflict() {
            throw new ConflictException("Conflict");
        }

        @GetMapping("/test/mail")
        void mail() {
            throw new MailSendException("SMTP unavailable");
        }

        @GetMapping("/test/required")
        void required(@RequestParam String value) {
        }
    }
}
