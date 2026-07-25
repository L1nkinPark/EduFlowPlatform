package com.lms.backend.service.impl;

import com.lms.backend.model.entity.OTP;
import com.lms.backend.repository.OTPRepository;
import com.lms.backend.repository.AccountRepository;
import com.lms.backend.model.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.security.SecureRandom;

@Service
public class OTPServiceImpl {
//
    private static final long OTP_EXPIRY_DURATION = 5 * 60 * 1000; // OTP hết hạn sau 5 phút
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

    // Phương thức tạo và gửi OTP
    public String generateAndSendOtp(String email) {
        String otp = String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));

        OTP otpEntity = new OTP();
        otpEntity.setEmail(email);
        otpEntity.setOtpCode(otp);
        otpEntity.setExpirationTime(System.currentTimeMillis() + OTP_EXPIRY_DURATION); // Đặt thời gian hết hạn
        otpRepository.save(otpEntity);
        try {
            emailService.sendOtpEmail(email, otp);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to deliver OTP email", e);
        }

        return otp;
    }

    // Phương thức xác minh OTP
    public String verifyOtp(String email, String otp) {
        Optional<OTP> otpEntityOptional = otpRepository.findByEmailAndOtpCode(email, otp);

        if (otpEntityOptional.isPresent()) {
            OTP otpEntity = otpEntityOptional.get();

            // Kiểm tra thời gian hết hạn của OTP
            if (otpEntity.getExpirationTime() > System.currentTimeMillis()) {
                otpRepository.delete(otpEntity); // Xóa OTP sau khi xác thực thành công
                return "OTP đã đúng";
            } else {
                return "OTP đã hết hạn";
            }
        }

        return "OTP sai";
    }

    // Phương thức reset mật khẩu
    public boolean resetPassword(String email, String newPassword) {
        Optional<Account> account = accountRepository.findByEmail(email);

        // Mã hóa mật khẩu mới
        String encodedPassword = passwordEncoder.encode(newPassword);

        if (account.isPresent()) {
            Account userAccount = account.get();
            userAccount.setPassword(encodedPassword);  // Cập nhật mật khẩu mới
            accountRepository.save(userAccount);   // Lưu lại thông tin người dùng
            return true;
        }
        return false; // Nếu email không tồn tại, trả về false
    }
}
