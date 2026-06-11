package com.lms.frontend.service;


import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.SubCategoryResponse;

import java.util.List;

public interface SubCategoryService {
    ApiResponse<List<SubCategoryResponse>> getAllCategories(int currentPage, int size);
}
