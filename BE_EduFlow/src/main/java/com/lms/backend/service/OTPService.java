package com.lms.backend.service;

public interface OTPService {
//
    String generateAndSendOtp(String email);

    boolean validateEmail(String email);

    String verifyOtp(String email, String otp);
}
