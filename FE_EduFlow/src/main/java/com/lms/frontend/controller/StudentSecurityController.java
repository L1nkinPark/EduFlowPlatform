package com.lms.frontend.controller;

import com.lms.frontend.model.request.ChangePasswordRequest;
import com.lms.frontend.model.response.AccountResponse;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentSecurityController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/security")
    public String showSecurityPage(Model model) {
        ApiResponse<AccountResponse> apiResponse = accountService.getMyProfile();
        AccountResponse account = (apiResponse != null && apiResponse.getPayload() != null)
                ? apiResponse.getPayload()
                : new AccountResponse();

        model.addAttribute("account", account);
        return "student-security";
    }

    @PostMapping("/security")
    public String changePassword(ChangePasswordRequest request, Model model) {
        if (request.getNewPassword() == null || !request.getNewPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không khớp.");
            return showSecurityPage(model);
        }

        ApiResponse<?> apiResponse = accountService.changeMyPassword(request);
        if (apiResponse == null || !"SUCCESS".equals(apiResponse.getStatus())) {
            model.addAttribute("error", apiResponse != null ? apiResponse.getMessage() : "Đổi mật khẩu thất bại.");
        } else {
            model.addAttribute("success", "Đổi mật khẩu thành công.");
        }

        return showSecurityPage(model);
    }

}
