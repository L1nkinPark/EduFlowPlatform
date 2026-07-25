package com.lms.frontend.controller;

import com.lms.frontend.model.request.ProfileUpdateRequest;
import com.lms.frontend.model.response.AccountResponse;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/student")
public class StudentEditController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/edit")
    public String showEditProfilePage(Model model) {
        ApiResponse<AccountResponse> apiResponse = accountService.getMyProfile();
        AccountResponse account = (apiResponse != null) ? apiResponse.getPayload() : null;
        if (account == null) {
            account = new AccountResponse();
        }

        model.addAttribute("account", account);
        if (!model.containsAttribute("profile")) {
            ProfileUpdateRequest profile = new ProfileUpdateRequest();
            profile.setFullName(account.getFullName());
            profile.setPhone(account.getPhone());
            if (account.getBirthday() != null && !account.getBirthday().isBlank()) {
                try {
                    profile.setBirthday(LocalDate.parse(account.getBirthday()));
                } catch (Exception ignored) {
                }
            }
            model.addAttribute("profile", profile);
        }

        return "student-edit-profile";
    }

    @PostMapping("/edit")
    public String updateProfile(ProfileUpdateRequest profile, Model model, HttpSession session) {
        ApiResponse<AccountResponse> apiResponse = accountService.updateMyProfile(profile);

        if (apiResponse == null || !"SUCCESS".equals(apiResponse.getStatus())) {
            model.addAttribute("error", apiResponse != null ? apiResponse.getMessage() : "Cập nhật thất bại. Vui lòng thử lại.");
        } else {
            model.addAttribute("success", "Cập nhật thông tin thành công.");
            // Cập nhật lại tên hiển thị trong session (dùng ở header) để phản ánh thay đổi ngay lập tức.
            AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
            if (userLogin != null && profile.getFullName() != null && !profile.getFullName().isBlank()) {
                userLogin.setFullName(profile.getFullName());
                session.setAttribute("userLogin", userLogin);
            }
        }

        return showEditProfilePage(model);
    }

}
