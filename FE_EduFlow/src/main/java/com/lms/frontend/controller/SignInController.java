package com.lms.frontend.controller;

import com.lms.frontend.model.request.LoginRequest;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.AccountService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/signin")
public class SignInController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String viewPage(Model model) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("");
        loginRequest.setPassword("");
        model.addAttribute("user", loginRequest);
        model.addAttribute("error", null);

        return "signin";
    }

    @PostMapping
    public String submitLogin(Model model, @Valid LoginRequest loginRequest,
                              BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", loginRequest);
            model.addAttribute("error", messageSource.getMessage(
                    "auth.login_required", null, LocaleContextHolder.getLocale()));
            return "signin";
        }

        ApiResponse<AuthResponse> apiResponse = accountService.login(loginRequest);
        if (apiResponse == null || !"SUCCESS".equals(apiResponse.getStatus())
                || apiResponse.getPayload() == null) {
            model.addAttribute("user", loginRequest);
            model.addAttribute("error", messageSource.getMessage(
                    "auth.login_invalid", null, LocaleContextHolder.getLocale()));
            return "signin";
        }

        // Get user data (this might include roles, or additional user information)
        AuthResponse authResponse = apiResponse.getPayload();

        // Store JWT in session
        session.setAttribute("userLogin", authResponse);

        if ("ADMIN".equals(authResponse.getRole())) {
            return "redirect:/admin";
        }

        if ("INSTRUCTOR".equals(authResponse.getRole())) {
            return "redirect:/instructor/mycourse";
        }

        return "redirect:/";
    }
}
