package com.lms.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MorePrivacyPolicyController {

    @GetMapping("/privacy/policy")
    public String showPrivacyPolicyPage(){
        return "privacy-policy";
    }
}
