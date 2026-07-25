package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.PublicStatsResponse;
import com.lms.frontend.service.PublicStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    @Autowired
    private PublicStatsService publicStatsService;

    @GetMapping("/about")
    public String showAboutPage(Model model) {
        ApiResponse<PublicStatsResponse> apiResponse = publicStatsService.getPublicStats();
        PublicStatsResponse stats = (apiResponse != null) ? apiResponse.getPayload() : null;
        if (stats == null) {
            stats = new PublicStatsResponse();
        }
        model.addAttribute("stats", stats);
        return "about";
    }
}
