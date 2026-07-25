package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CategoryResponse;
import com.lms.frontend.model.response.CourseResponse;
import com.lms.frontend.model.response.SubCategoryResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.CategoryService;
import com.lms.frontend.service.OrderService;
import com.lms.frontend.service.SubCategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private OrderService orderService;

    @ModelAttribute("categories")
    public List<CategoryResponse> populateCategories() {
        try {
            ApiResponse<List<CategoryResponse>> catResponse = categoryService.getAllCategories(1, 100);
            if (catResponse != null && catResponse.getPayload() != null) {
                return catResponse.getPayload();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

    @ModelAttribute("subCategories")
    public List<SubCategoryResponse> populateSubCategories() {
        try {
            ApiResponse<List<SubCategoryResponse>> response =
                    subCategoryService.getAllCategories(1, 500);
            if (response != null && response.getPayload() != null) {
                return response.getPayload();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Provides the set of course IDs that the logged-in user has purchased.
     * Available in every template as {@code enrolledCourseIds}.
     */
    @ModelAttribute("enrolledCourseIds")
    public Set<String> populateEnrolledCourseIds(HttpSession session) {
        try {
            AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
            if (userLogin != null) {
                ApiResponse<List<CourseResponse>> response = orderService.getUserCourses();
                if (response != null && response.getPayload() != null) {
                    return response.getPayload().stream()
                            .map(CourseResponse::getCourseId)
                            .collect(Collectors.toSet());
                }
            }
        } catch (Exception ignored) {
        }
        return Collections.emptySet();
    }
}
