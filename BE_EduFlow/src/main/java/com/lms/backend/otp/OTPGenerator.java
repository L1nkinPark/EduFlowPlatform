package com.lms.backend.otp;

import java.security.SecureRandom;

public class OTPGenerator {
//
    private static final String NUMERIC_STRING = "0123456789";
    private static final int OTP_LENGTH = 6; // Độ dài OTP

    public static String generateOTP() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(NUMERIC_STRING.charAt(random.nextInt(NUMERIC_STRING.length())));
        }

        return otp.toString();
    }
}
