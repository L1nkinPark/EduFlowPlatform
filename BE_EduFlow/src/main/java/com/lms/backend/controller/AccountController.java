package com.lms.backend.controller;

import com.lms.backend.exception.ForbiddenException;
import com.lms.backend.model.entity.Account;
import com.lms.backend.model.mapper.AccountMapper;
import com.lms.backend.model.request.ChangePasswordRequest;
import com.lms.backend.model.request.ProfileUpdateRequest;
import com.lms.backend.model.response.AccountResponse;
import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.repository.AccountRepository;
import com.lms.backend.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Real account-management endpoints for the currently authenticated user
 * (any role): view own profile, update profile details, change password,
 * and delete (deactivate) own account. Replaces the previously disabled
 * placeholder controller.
 */
@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Account requireAccount(CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new ForbiddenException("Authentication required.");
        }
        return userDetails.getAccount();
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Account account = requireAccount(userDetails);
        ApiResponse response = new ApiResponse();
        response.ok("OK", accountMapper.convertToDTO(account));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse> updateMyProfile(@RequestBody ProfileUpdateRequest request,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Account account = requireAccount(userDetails);
        ApiResponse response = new ApiResponse();

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            account.setFullName(request.getFullName().trim());
        }
        if (request.getPhone() != null) {
            account.setPhone(request.getPhone().trim());
        }
        if (request.getBirthday() != null) {
            account.setBirthday(request.getBirthday());
        }

        Account saved = accountRepository.save(account);
        response.ok("Profile updated successfully", accountMapper.convertToDTO(saved));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me/password")
    public ResponseEntity<ApiResponse> changeMyPassword(@RequestBody ChangePasswordRequest request,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        Account account = requireAccount(userDetails);
        ApiResponse response = new ApiResponse();

        if (request.getCurrentPassword() == null || !passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())) {
            response.error("Current password is incorrect.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (request.getNewPassword() == null || request.getNewPassword().length() < 6) {
            response.error("New password must be at least 6 characters long.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);

        response.ok("Password changed successfully");
        return ResponseEntity.ok(response);
    }

    // Học viên tự đóng (deactivate) tài khoản của chính mình. Không xoá cứng dữ
    // liệu (giữ lại lịch sử order/progress), chỉ đặt status = false, tương tự
    // cách các account bị khoá bởi admin.
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse> deactivateMyAccount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Account account = requireAccount(userDetails);
        ApiResponse response = new ApiResponse();

        account.setStatus(false);
        accountRepository.save(account);

        response.ok("Account deactivated successfully");
        return ResponseEntity.ok(response);
    }
}
