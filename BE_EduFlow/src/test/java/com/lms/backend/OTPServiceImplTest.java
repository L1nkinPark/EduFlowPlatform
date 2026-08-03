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
import org.springframework.mail.MailSendException;

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
    void testValidateEmail_NormalizesCaseAndWhitespace() {
        when(accountRepository.findByEmail("user@example.com")).thenReturn(Optional.of(new Account()));

        assertTrue(otpService.validateEmail("  USER@EXAMPLE.COM "));

        verify(accountRepository).findByEmail("user@example.com");
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
        verify(otpRepository).deleteByEmailAndPurpose(email, OTPServiceImpl.PURPOSE_SIGNUP);
        verify(otpRepository, times(1)).save(any(OTP.class));
        verify(emailService, times(1)).sendOtpEmail(eq(email), anyString());
    }

    @Test
    void testGenerateAndSendOtp_PropagatesEmailFailure() {
        String email = "user@example.com";
        doThrow(new MailSendException("SMTP unavailable"))
                .when(emailService).sendOtpEmail(eq(email), anyString());

        assertThrows(MailSendException.class, () -> otpService.generateAndSendOtp(email));

        verify(otpRepository).deleteByEmailAndPurpose(email, OTPServiceImpl.PURPOSE_SIGNUP);
        verify(otpRepository).save(any(OTP.class));
        verify(emailService).sendOtpEmail(eq(email), anyString());
    }

    @Test
    void testVerifyOtp_Valid() {
        String email = "user@example.com";
        String otp = "123456";
        OTP otpEntity = new OTP();
        otpEntity.setOtpCode(otp);
        otpEntity.setExpirationTime(System.currentTimeMillis() + 5000); // OTP chưa hết hạn

        when(otpRepository.findByEmailAndOtpCodeAndPurpose(email, otp, OTPServiceImpl.PURPOSE_SIGNUP))
                .thenReturn(Optional.of(otpEntity));

        String result = otpService.verifyOtp(email, otp);

        assertEquals("OTP đã đúng", result);
        verify(otpRepository).findByEmailAndOtpCodeAndPurpose(email, otp, OTPServiceImpl.PURPOSE_SIGNUP);
        verify(otpRepository).save(otpEntity);
        assertNull(otpEntity.getOtpCode());
        assertNotNull(otpEntity.getVerificationToken());
    }

    @Test
    void testVerifyOtp_Expired() {
        String email = "user@example.com";
        String otp = "123456";
        OTP otpEntity = new OTP();
        otpEntity.setOtpCode(otp);
        otpEntity.setExpirationTime(System.currentTimeMillis() - 1000); // OTP đã hết hạn

        when(otpRepository.findByEmailAndOtpCodeAndPurpose(email, otp, OTPServiceImpl.PURPOSE_SIGNUP))
                .thenReturn(Optional.of(otpEntity));

        String result = otpService.verifyOtp(email, otp);

        assertEquals("OTP sai hoặc đã hết hạn", result);
        verify(otpRepository).findByEmailAndOtpCodeAndPurpose(email, otp, OTPServiceImpl.PURPOSE_SIGNUP);
        verify(otpRepository).delete(otpEntity);
    }

    @Test
    void testVerifyOtp_Invalid() {
        String email = "user@example.com";
        String otp = "123456";

        when(otpRepository.findByEmailAndOtpCodeAndPurpose(email, otp, OTPServiceImpl.PURPOSE_SIGNUP))
                .thenReturn(Optional.empty());

        String result = otpService.verifyOtp(email, otp);

        assertEquals("OTP sai hoặc đã hết hạn", result);
        verify(otpRepository).findByEmailAndOtpCodeAndPurpose(email, otp, OTPServiceImpl.PURPOSE_SIGNUP);
    }

    @Test
    void resetPasswordRequiresAndConsumesPasswordResetToken() {
        String email = "user@example.com";
        String token = "one-time-token";
        OTP verification = new OTP();
        verification.setEmail(email);
        verification.setPurpose(OTPServiceImpl.PURPOSE_PASSWORD_RESET);
        verification.setVerificationToken(token);
        verification.setExpirationTime(System.currentTimeMillis() + 60_000);
        Account account = new Account();
        when(otpRepository.findByEmailAndPurposeAndVerificationToken(
                email, OTPServiceImpl.PURPOSE_PASSWORD_RESET, token)).thenReturn(Optional.of(verification));
        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));
        when(passwordEncoder.encode("new-password")).thenReturn("encoded");

        assertTrue(otpService.resetPassword(email, "new-password", token));
        assertEquals("encoded", account.getPassword());
        verify(otpRepository).delete(verification);
        verify(accountRepository).save(account);
    }

    @Test
    void resetPasswordRejectsMissingVerificationToken() {
        assertFalse(otpService.resetPassword("user@example.com", "new-password", null));
        verifyNoInteractions(accountRepository);
    }
}
