package com.lms.frontend.controller;

import com.lms.frontend.web.UserRegister;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignUpController {

    @GetMapping("/signup")
    public String showSignUpPage(Model model) {
        model.addAttribute("userRegister", new UserRegister());
        return "signup";
    }
}
