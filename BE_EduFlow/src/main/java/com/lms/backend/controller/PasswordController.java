package com.lms.backend.controller;

import com.lms.backend.service.impl.AccountServiceImpl;
import com.lms.backend.service.impl.OTPServiceImpl;
import com.lms.backend.model.request.PasswordResetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
public class PasswordController {
//
    @Autowired
    private OTPServiceImpl otpService;

    @Autowired
    private AccountServiceImpl accountService;

    // Endpoint để yêu cầu reset mật khẩu
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
        boolean isReset = otpService.resetPassword(request.getEmail(), request.getNewPassword());
        if (isReset) {
            return ResponseEntity.ok("Password reset successfully.");
        } else {
            return ResponseEntity.status(400).body("Failed to reset password.");
        }
    }


    // Kiểm tra email và gửi OTP nếu tồn tại
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        if (!otpService.validateEmail(email)) {
            return ResponseEntity.badRequest().body("Email not found.");
        }

        otpService.generateAndSendOtp(email);
        return ResponseEntity.ok("OTP has been sent to your email.");
    }

    // Xác minh OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        String result = otpService.verifyOtp(email, otp);
        if ("OTP đã đúng".equals(result)) {
            return ResponseEntity.ok("OTP verified successfully.");
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

}
