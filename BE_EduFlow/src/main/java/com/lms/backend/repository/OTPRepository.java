package com.lms.backend.repository;

import com.lms.backend.model.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP, Long> {
//
    Optional<OTP> findByEmailAndOtpCode(String email, String otpCode);
    Optional<OTP> findByEmailAndOtpCodeAndPurpose(String email, String otpCode, String purpose);
    Optional<OTP> findByEmailAndPurposeAndVerificationToken(String email, String purpose, String verificationToken);
    Optional<OTP> findByEmail(String email);
    void deleteByEmail(String email);
    void deleteByEmailAndPurpose(String email, String purpose);

    void delete(OTP otpEntity);
}
