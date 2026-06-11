package com.lms.backend;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.entity.OTP;
import com.lms.backend.repository.AccountRepository;
import com.lms.backend.repository.OTPRepository;
import com.lms.backend.service.impl.EmailService;
import com.lms.backend.service.impl.OTPServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OTPServiceImplTest {
//
    @Mock
    private OTPRepository otpRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private OTPServiceImpl otpService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateEmail_Valid() {
        String email = "user@example.com";
        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(new Account()));

        boolean result = otpService.validateEmail(email);

        assertTrue(result);
        verify(accountRepository, times(1)).findByEmail(email);
    }

    @Test
    void testValidateEmail_Invalid() {
        String email = "invalid@example.com";
        when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());

        boolean result = otpService.validateEmail(email);

        assertFalse(result);
        verify(accountRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGenerateAndSendOtp_Success() {
        String email = "user@example.com";
        OTP otpEntity = new OTP();
        otpEntity.setOtpCode("123456");

        doNothing().when(emailService).sendOtpEmail(email, "123456");

        String otp = otpService.generateAndSendOtp(email);

        assertNotNull(otp);
        assertEquals(6, otp.length());
        verify(otpRepository, times(1)).save(any(OTP.class));
        verify(emailService, times(1)).sendOtpEmail(eq(email), anyString());
    }

    @Test
    void testVerifyOtp_Valid() {
        String email = "user@example.com";
        String otp = "123456";
        OTP otpEntity = new OTP();
        otpEntity.setOtpCode(otp);
        otpEntity.setExpirationTime(System.currentTimeMillis() + 5000); // OTP chưa hết hạn

        when(otpRepository.findByEmailAndOtpCode(email, otp)).thenReturn(Optional.of(otpEntity));

        String result = otpService.verifyOtp(email, otp);

        assertEquals("OTP đã đúng", result);
        verify(otpRepository, times(1)).findByEmailAndOtpCode(email, otp);
        verify(otpRepository, times(1)).delete(otpEntity);
    }

    @Test
    void testVerifyOtp_Expired() {
        String email = "user@example.com";
        String otp = "123456";
        OTP otpEntity = new OTP();
        otpEntity.setOtpCode(otp);
        otpEntity.setExpirationTime(System.currentTimeMillis() - 1000); // OTP đã hết hạn

        when(otpRepository.findByEmailAndOtpCode(email, otp)).thenReturn(Optional.of(otpEntity));

        String result = otpService.verifyOtp(email, otp);

        assertEquals("OTP đã hết hạn", result);
        verify(otpRepository, times(1)).findByEmailAndOtpCode(email, otp);
        verify(otpRepository, never()).delete(otpEntity);
    }

    @Test
    void testVerifyOtp_Invalid() {
        String email = "user@example.com";
        String otp = "123456";

        when(otpRepository.findByEmailAndOtpCode(email, otp)).thenReturn(Optional.empty());

        String result = otpService.verifyOtp(email, otp);

        assertEquals("OTP sai", result);
        verify(otpRepository, times(1)).findByEmailAndOtpCode(email, otp);
    }
}
