package com.lms.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentDeleteController {

    @GetMapping("/delete")
    public String showDeleteProfilePage(){
        return "student-delete-profile";
    }

}
