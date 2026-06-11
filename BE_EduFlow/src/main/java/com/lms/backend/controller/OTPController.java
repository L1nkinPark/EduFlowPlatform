package com.lms.backend.controller;

import com.lms.backend.service.AccountService;
import com.lms.backend.service.impl.OTPServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp") // Đảm bảo rằng đường dẫn bắt đầu với /api/otp
public class OTPController {
//
    @Autowired
    private OTPServiceImpl otpService;


    // Gửi OTP
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        if (otpService.validateEmail(email)) {
            otpService.generateAndSendOtp(email);
            return ResponseEntity.ok("OTP has been sent to your email.");
        }
        return ResponseEntity.status(400).body("Email not found.");
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
