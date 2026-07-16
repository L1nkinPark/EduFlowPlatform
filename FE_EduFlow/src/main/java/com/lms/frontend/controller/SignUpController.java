package com.lms.frontend.controller;

import com.lms.frontend.model.request.SignUpRequest;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.service.AccountService;
import com.lms.frontend.web.UserRegister;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignUpController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/signup")
    public String showSignUpPage(Model model) {
        model.addAttribute("userRegister", new UserRegister());
        return "signup";
    }

    @PostMapping("/register/process")
    public String registerProcess(Model model, @Valid UserRegister userRegister, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userRegister", userRegister);
            model.addAttribute("error", "Vui lòng điền đầy đủ thông tin hợp lệ.");
            return "signup";
        }

        if (!userRegister.getPassword().equals(userRegister.getConfirmPassword())) {
            model.addAttribute("userRegister", userRegister);
            model.addAttribute("error", "Mật khẩu và xác nhận mật khẩu không khớp.");
            return "signup";
        }

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .fullName(userRegister.getFirstName() + " " + userRegister.getLastName())
                .email(userRegister.getEmail())
                .username(userRegister.getEmail())
                .role("STUDENT")
                .password(userRegister.getPassword())
                .build();

        ApiResponse apiResponse = accountService.register(signUpRequest);
        if (apiResponse == null || !"SUCCESS".equals(apiResponse.getStatus())) {
            model.addAttribute("userRegister", userRegister);
            model.addAttribute("error", apiResponse != null ? apiResponse.getMessage() : "Đăng ký thất bại. Vui lòng thử lại.");
            return "signup";
        }

        return "redirect:/signin?register_success=true";
    }
}
