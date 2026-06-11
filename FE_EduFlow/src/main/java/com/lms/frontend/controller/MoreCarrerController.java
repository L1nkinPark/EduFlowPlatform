package com.lms.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoreCarrerController {

    @GetMapping("/career")
    public String showCarrerPage(){
        return "careers";
    }

}
