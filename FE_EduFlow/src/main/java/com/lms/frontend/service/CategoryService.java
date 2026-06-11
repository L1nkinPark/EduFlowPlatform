package com.lms.frontend.service;


import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    ApiResponse<List<CategoryResponse>> getAllCategories(int currentPage, int size);
}
