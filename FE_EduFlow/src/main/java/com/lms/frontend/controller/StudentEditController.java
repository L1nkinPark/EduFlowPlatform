package com.lms.frontend.controller;

import com.lms.frontend.model.response.AuthResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentEditController {

    @GetMapping("/edit")
    public String showEditProfilePage(Model model, HttpSession session){
        AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
        model.addAttribute("userLogin", userLogin);
        return "student-edit-profile";
    }

}
