package com.lms.backend.validator;

import com.lms.backend.model.entity.Category;
import com.lms.backend.model.request.CategoryRequest;
import com.lms.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CategoryValidator implements Validator {
    @Autowired
    private CategoryService categoryService;

    @Override
    public boolean supports(Class<?> clazz) {
        return CategoryRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CategoryRequest  categoryRequest = (CategoryRequest) target;
        Category category = categoryService.findByCategoryName(categoryRequest.getCategoryName().trim().toLowerCase());
        if(category != null){
            errors.rejectValue("categoryName", "Category name has already existed.");
        }
    }
}
