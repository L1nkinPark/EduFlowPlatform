package com.lms.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoreTestimonialController {

    @GetMapping("/testimonial")
    public String showTestimonialPage(){
        return "testimonials";
    }

}
