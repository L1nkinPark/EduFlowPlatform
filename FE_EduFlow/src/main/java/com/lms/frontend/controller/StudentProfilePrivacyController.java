package com.lms.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentProfilePrivacyController {

    @GetMapping("/profile/privacy")
    public String showProfilePrivacyPage(){
        return "student-profile-privacy";
    }

}
