package com.lms.backend.service.impl;

import com.lms.backend.model.entity.OTP;
import com.lms.backend.repository.OTPRepository;
import com.lms.backend.repository.AccountRepository;
import com.lms.backend.model.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

@Service
public class OTPServiceImpl {
    public static final String PURPOSE_SIGNUP = "SIGNUP";
    public static final String PURPOSE_PASSWORD_RESET = "PASSWORD_RESET";
    private static final long OTP_EXPIRY_DURATION = 5 * 60 * 1000; // OTP hết hạn sau 5 phút
    private static final long VERIFICATION_EXPIRY_DURATION = 10 * 60 * 1000;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private AccountRepository accountRepository;  // Kiểm tra email trong cơ sở dữ liệu

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    // Phương thức để kiểm tra email tồn tại trong cơ sở dữ liệu
    public boolean validateEmail(String email) {
        Optional<Account> account = accountRepository.findByEmail(email);
        return account.isPresent();  // Trả về true nếu tồn tại, false nếu không
    }

    // Lưu OTP và gửi email trong cùng transaction. MailException là runtime
    // exception nên bản ghi OTP sẽ được rollback nếu gửi mail thất bại.
    @Transactional
    public String generateAndSendOtp(String email) {
        return generateAndSendOtp(email, PURPOSE_SIGNUP);
    }

    @Transactional
    public String generateAndSendOtp(String email, String purpose) {
        String normalizedEmail = normalizeEmail(email);
        String normalizedPurpose = requirePurpose(purpose);
        String otp = String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));

        OTP otpEntity = new OTP();
        otpEntity.setEmail(normalizedEmail);
        otpEntity.setOtpCode(otp);
        otpEntity.setPurpose(normalizedPurpose);
        otpEntity.setExpirationTime(System.currentTimeMillis() + OTP_EXPIRY_DURATION); // Đặt thời gian hết hạn
        otpRepository.deleteByEmailAndPurpose(normalizedEmail, normalizedPurpose);
        otpRepository.save(otpEntity);
        emailService.sendOtpEmail(normalizedEmail, otp);

        return otp;
    }

    @Transactional
    public Optional<String> verifyOtpAndIssueToken(String email, String otp, String purpose) {
        String normalizedEmail = normalizeEmail(email);
        String normalizedPurpose = requirePurpose(purpose);
        Optional<OTP> otpEntityOptional = otpRepository.findByEmailAndOtpCodeAndPurpose(
                normalizedEmail, otp, normalizedPurpose);

        if (otpEntityOptional.isEmpty()) {
            return Optional.empty();
        }

        OTP otpEntity = otpEntityOptional.get();
        if (otpEntity.getExpirationTime() == null
                || otpEntity.getExpirationTime() <= System.currentTimeMillis()) {
            otpRepository.delete(otpEntity);
            return Optional.empty();
        }

        String verificationToken = UUID.randomUUID().toString();
        otpEntity.setOtpCode(null);
        otpEntity.setVerificationToken(verificationToken);
        otpEntity.setExpirationTime(System.currentTimeMillis() + VERIFICATION_EXPIRY_DURATION);
        otpRepository.save(otpEntity);
        return Optional.of(verificationToken);
    }

    @Transactional
    public boolean consumeVerification(String email, String purpose, String verificationToken) {
        if (verificationToken == null || verificationToken.isBlank()) {
            return false;
        }
        Optional<OTP> verification = otpRepository.findByEmailAndPurposeAndVerificationToken(
                normalizeEmail(email), requirePurpose(purpose), verificationToken);
        if (verification.isEmpty()) {
            return false;
        }

        OTP entity = verification.get();
        otpRepository.delete(entity);
        return entity.getExpirationTime() != null
                && entity.getExpirationTime() > System.currentTimeMillis();
    }

    // Backward-compatible service method used by internal callers and tests.
    public String verifyOtp(String email, String otp) {
        return verifyOtpAndIssueToken(email, otp, PURPOSE_SIGNUP).isPresent()
                ? "OTP đã đúng" : "OTP sai hoặc đã hết hạn";
    }

    // Phương thức reset mật khẩu
    @Transactional
    public boolean resetPassword(String email, String newPassword, String verificationToken) {
        String normalizedEmail = normalizeEmail(email);
        if (!consumeVerification(normalizedEmail, PURPOSE_PASSWORD_RESET, verificationToken)) {
            return false;
        }
        Optional<Account> account = accountRepository.findByEmail(normalizedEmail);

        if (account.isPresent()) {
            Account userAccount = account.get();
            userAccount.setPassword(passwordEncoder.encode(newPassword));
            accountRepository.save(userAccount);   // Lưu lại thông tin người dùng
            return true;
        }
        return false; // Nếu email không tồn tại, trả về false
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        return email.trim().toLowerCase();
    }

    private String requirePurpose(String purpose) {
        if (!PURPOSE_SIGNUP.equals(purpose) && !PURPOSE_PASSWORD_RESET.equals(purpose)) {
            throw new IllegalArgumentException("Invalid OTP purpose");
        }
        return purpose;
    }
}
