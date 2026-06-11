package com.lms.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminEarnController {

    @GetMapping("/earn")
    public String showEarnPage() {
        return "admin-earnings";
    }

}
