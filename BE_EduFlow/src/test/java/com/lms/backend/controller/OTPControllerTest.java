package com.lms.backend.controller;

import com.lms.backend.service.impl.OTPServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OTPControllerTest {

    private OTPServiceImpl otpService;
    private OTPController controller;

    @BeforeEach
    void setUp() {
        otpService = mock(OTPServiceImpl.class);
        controller = new OTPController(otpService);
    }

    @Test
    void sendOtpSignupReturnsOkAfterEmailIsSent() {
        String email = "new-user@example.com";
        when(otpService.validateEmail(email)).thenReturn(false);

        ResponseEntity<String> response = controller.sendOtpSignup(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(otpService).generateAndSendOtp(email);
    }

    @Test
    void sendOtpSignupReturnsServiceUnavailableWhenSmtpAuthenticationFails() {
        String email = "new-user@example.com";
        when(otpService.validateEmail(email)).thenReturn(false);
        doThrow(new MailAuthenticationException("Invalid SMTP credentials"))
                .when(otpService).generateAndSendOtp(email);

        ResponseEntity<String> response = controller.sendOtpSignup(email);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Không thể gửi email OTP. Vui lòng thử lại sau.", response.getBody());
    }

    @Test
    void sendOtpSignupRejectsRegisteredEmail() {
        String email = "existing-user@example.com";
        when(otpService.validateEmail(email)).thenReturn(true);

        ResponseEntity<String> response = controller.sendOtpSignup(email);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
