package com.lms.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/instructor")
public class InstructorReviewController {

    @GetMapping("/reviews")
    public String showReviewPage() {
        return "instructor-reviews";
    }

}
