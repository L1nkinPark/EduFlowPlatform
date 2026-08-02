package com.lms.backend.controller;

import com.lms.backend.service.impl.OTPServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp") // Đảm bảo rằng đường dẫn bắt đầu với /api/otp
public class OTPController {

    private final OTPServiceImpl otpService;

    public OTPController(OTPServiceImpl otpService) {
        this.otpService = otpService;
    }

    // Gửi OTP
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        if (otpService.validateEmail(email)) {
            try {
                otpService.generateAndSendOtp(email);
                return ResponseEntity.ok("OTP has been sent to your email.");
            } catch (MailException e) {
                return emailServiceUnavailable();
            }
        }
        return ResponseEntity.status(400).body("Email not found.");
    }

    // Gửi OTP Đăng ký
    @PostMapping("/send-otp-signup")
    public ResponseEntity<String> sendOtpSignup(@RequestParam String email) {
        if (otpService.validateEmail(email)) {
            return ResponseEntity.status(400).body("Email already registered.");
        }
        try {
            otpService.generateAndSendOtp(email);
            return ResponseEntity.ok("OTP has been sent to your email.");
        } catch (MailException e) {
            return emailServiceUnavailable();
        }
    }

    private ResponseEntity<String> emailServiceUnavailable() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Không thể gửi email OTP. Vui lòng thử lại sau.");
    }

    // Kiểm tra email hợp lệ
    @PostMapping("/validate-email")  // Kiểm tra email qua POST
    public ResponseEntity<Boolean> validateEmail(@RequestParam String email) {
        boolean emailExists = otpService.validateEmail(email);  // Kiểm tra email
        return ResponseEntity.ok(emailExists);  // Trả về true/false
    }


    // Xác minh OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        String verificationResult = otpService.verifyOtp(email, otp);
        if ("OTP đã đúng".equals(verificationResult)) {
            return ResponseEntity.ok("OTP verified successfully.");
        } else {
            return ResponseEntity.status(400).body("Invalid or expired OTP.");
        }
    }


}
