package com.lms.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoreFAQController {

    @GetMapping("/faq")
    public String showFAQPage(){
        return "faq";
    }

}
