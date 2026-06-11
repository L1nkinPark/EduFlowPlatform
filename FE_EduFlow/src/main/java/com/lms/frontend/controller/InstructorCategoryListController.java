package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CategoryResponse;
import com.lms.frontend.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/instructor/categories")
public class InstructorCategoryListController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/list")
    public String viewInstructorCategories(Model model, @RequestParam(required = false, defaultValue = "1") Integer currentPage,
                                           @RequestParam(required = false, defaultValue = "5") Integer size, HttpSession session) {
//        AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");

        ApiResponse<List<CategoryResponse>> apiResponse = categoryService.getAllCategories(currentPage, size);

        List<CategoryResponse> categoryList = null;
        Map<String, Object> metadata = null;

        if (apiResponse == null) {
            categoryList = new ArrayList<>();
            metadata = new HashMap<>();
        } else {
            categoryList = apiResponse.getPayload();
            metadata = apiResponse.getMetadata();
        }
        model.addAttribute("categoryList", categoryList);

        model.addAttribute("totalPages", metadata.get("totalPages"));
        model.addAttribute("totalElements", metadata.get("totalElements"));
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("size", size);

        return "instructormng-category-list";
    }
}
