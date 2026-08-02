package com.lms.backend.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    private EmailService emailService;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        emailService = new EmailService(mailSender, "sender@example.com");
    }

    @Test
    void testSendOtpEmail() {
        String toEmail = "test@example.com";
        String otp = "123456";

        emailService.sendOtpEmail(toEmail, otp);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals("sender@example.com", sentMessage.getFrom());
        assertEquals(toEmail, Objects.requireNonNull(sentMessage.getTo())[0]);
        assertEquals("Your OTP Code", sentMessage.getSubject());
        assertEquals("Your OTP code is: 123456\nThis code will expire in 5 minutes.", sentMessage.getText());
    }
}
