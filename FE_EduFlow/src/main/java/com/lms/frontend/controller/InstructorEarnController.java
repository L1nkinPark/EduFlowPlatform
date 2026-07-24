package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.InstructorDashboardResponse;
import com.lms.frontend.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/instructor")
public class InstructorEarnController {

    @Autowired
    private InstructorService instructorService;

    @GetMapping("/earn")
    public String showEarnPage(Model model) {
        ApiResponse<InstructorDashboardResponse> apiResponse = instructorService.getDashboard();
        InstructorDashboardResponse dashboard = (apiResponse != null && apiResponse.getPayload() != null)
                ? apiResponse.getPayload()
                : new InstructorDashboardResponse();

        model.addAttribute("dashboard", dashboard);
        return "instructor-earnings";
    }

}
