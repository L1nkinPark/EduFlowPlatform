package com.lms.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/instructor")
public class InstructorProfileController {

    // "/instructor/profile" formerly rendered a page with hardcoded fake stats
    // ("Abhay Rai", static revenue numbers). My Courses is now the real
    // instructor landing page, so redirect any old bookmarks/links there.
    @GetMapping("/profile")
    public String showProfilePage() {
        return "redirect:/instructor/mycourse";
    }

}
