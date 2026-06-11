package com.lms.backend.security;

import jakarta.xml.bind.DatatypeConverter;

import java.security.SecureRandom;

public class JwtMain {

    public static void main(String[] args) {
        // Độ dài của chuỗi khóa bí mật (ở đây là 64 ký tự hex, tương đương 32 byte)
        int keyLengthBytes = 32;

        // Tạo một mảng byte ngẫu nhiên
        byte[] randomBytes = new byte[keyLengthBytes];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);

        // Chuyển đổi mảng byte thành chuỗi hex
        String secretKey = DatatypeConverter.printHexBinary(randomBytes);

        System.out.println("SECRET_KEY: " + secretKey);
    }

}

