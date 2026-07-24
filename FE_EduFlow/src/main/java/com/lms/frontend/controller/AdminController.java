package com.lms.frontend.controller;

import com.lms.frontend.model.response.AdminDashboardResponse;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/admin")
    public String dashboard(Model model) {
        ApiResponse<AdminDashboardResponse> apiResponse = adminService.getDashboard();
        AdminDashboardResponse dashboard = (apiResponse != null && apiResponse.getPayload() != null)
                ? apiResponse.getPayload()
                : new AdminDashboardResponse();

        model.addAttribute("dashboard", dashboard);
        return "admin";
    }
}
