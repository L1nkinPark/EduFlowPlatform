package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.service.AccountService;
import com.lms.frontend.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentDeleteController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/delete")
    public String showDeleteProfilePage(Model model) {
        ApiResponse<List<CourseResponse>> apiResponse = orderService.getUserCourses();
        int purchasedCount = (apiResponse != null && apiResponse.getPayload() != null)
                ? apiResponse.getPayload().size()
                : 0;

        model.addAttribute("purchasedCount", purchasedCount);
        return "student-delete-profile";
    }

    @PostMapping("/delete")
    public String deactivateAccount(Model model, HttpSession session) {
        ApiResponse<?> apiResponse = accountService.deactivateMyAccount();

        if (apiResponse == null || !"SUCCESS".equals(apiResponse.getStatus())) {
            model.addAttribute("error", apiResponse != null ? apiResponse.getMessage() : "Đóng tài khoản thất bại.");
            return showDeleteProfilePage(model);
        }

        session.invalidate();
        return "redirect:/";
    }

}
