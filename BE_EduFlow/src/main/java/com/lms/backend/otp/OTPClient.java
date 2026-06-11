package com.lms.backend.otp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OTPClient {
//
    public static void requestOTP(String email) {
        try {
            URL url = new URL("http://localhost:8888/api/otp/send?email=" + email);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Gửi request
            try (OutputStream os = conn.getOutputStream()) {
                os.write("".getBytes());
                os.flush();
            }

            // Đọc response
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String output;
            while ((output = br.readLine()) != null) {
                System.out.println("Response: " + output);
            }

            conn.disconnect();
            System.out.println("OTP đã được gửi đến email của bạn");
        } catch (Exception e) {
            System.out.println("Lỗi khi gửi OTP: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Phương thức gửi yêu cầu xác thực OTP
    public static void verifyOTP(String email, String otpCode) {
        try {
            URL url = new URL("http://localhost:8888/api/otp/verify?email=" + email + "&otpCode=" + otpCode);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Gửi request
            try (OutputStream os = conn.getOutputStream()) {
                os.write("".getBytes());
                os.flush();
            }

            // Đọc response
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String output;
            StringBuilder response = new StringBuilder();
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            conn.disconnect();

            // Kiểm tra kết quả xác thực
            if (Boolean.parseBoolean(response.toString())) {
                System.out.println("Xác thực OTP thành công!");
            } else {
                System.out.println("OTP không hợp lệ hoặc đã hết hạn!");
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi xác thực OTP: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Gửi yêu cầu OTP
        requestOTP("voduchieu42@gmail.com");
    }


}
