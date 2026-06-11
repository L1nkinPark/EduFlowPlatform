package com.lms.frontend.controller;

import com.lms.frontend.web.UserForgot;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForgotController {

    @GetMapping("/forgot")
    public String showForgotPage(Model model) {
        model.addAttribute("userForgot", new UserForgot());
        return "forgot";
    }
}
