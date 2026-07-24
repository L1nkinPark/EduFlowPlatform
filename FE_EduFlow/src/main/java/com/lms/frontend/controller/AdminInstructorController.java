package com.lms.frontend.controller;

import com.lms.frontend.model.request.SignUpRequest;
import com.lms.frontend.model.response.AccountResponse;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin/instructors")
public class AdminInstructorController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public String listInstructors(Model model) {
        ApiResponse<List<AccountResponse>> apiResponse = adminService.getAccountsByRole("INSTRUCTOR");
        List<AccountResponse> instructors = (apiResponse != null && apiResponse.getPayload() != null)
                ? apiResponse.getPayload()
                : Collections.emptyList();

        model.addAttribute("instructors", instructors);
        if (!model.containsAttribute("newInstructor")) {
            model.addAttribute("newInstructor", new SignUpRequest());
        }
        return "admin-instructors";
    }

    @PostMapping("/create")
    public String createInstructor(@Valid @ModelAttribute("newInstructor") SignUpRequest newInstructor,
                                    BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Vui lòng điền đầy đủ thông tin hợp lệ.");
            return listInstructors(model);
        }

        newInstructor.setUsername(newInstructor.getUsername() != null ? newInstructor.getUsername() : newInstructor.getEmail());
        ApiResponse<?> apiResponse = adminService.createInstructor(newInstructor);

        if (apiResponse == null || !"SUCCESS".equals(apiResponse.getStatus())) {
            model.addAttribute("error", apiResponse != null ? apiResponse.getMessage() : "Tạo tài khoản instructor thất bại.");
            return listInstructors(model);
        }

        model.addAttribute("success", "Tạo tài khoản instructor thành công: " + newInstructor.getUsername());
        return listInstructors(model);
    }
}
