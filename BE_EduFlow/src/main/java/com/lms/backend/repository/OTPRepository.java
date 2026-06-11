package com.lms.backend.repository;

import com.lms.backend.model.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP, Long> {
//
    Optional<OTP> findByEmailAndOtpCode(String email, String otpCode);
    Optional<OTP> findByEmail(String email);
    void deleteByEmail(String email);

    void delete(OTP otpEntity);
}
