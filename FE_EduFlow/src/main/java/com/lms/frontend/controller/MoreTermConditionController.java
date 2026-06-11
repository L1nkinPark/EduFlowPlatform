package com.lms.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoreTermConditionController {

    @GetMapping("/term-condition")
    public String showTermsAndConditionsPage(){
        return "terms-and-conditions";
    }

}
