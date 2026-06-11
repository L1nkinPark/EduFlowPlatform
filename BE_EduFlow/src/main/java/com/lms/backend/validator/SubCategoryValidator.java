package com.lms.backend.validator;

import com.lms.backend.model.entity.SubCategory;
import com.lms.backend.model.request.SubCategoryRequest;
import com.lms.backend.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SubCategoryValidator implements Validator {
    @Autowired
    private SubCategoryService subCategoryService;

    @Override
    public boolean supports(Class<?> clazz) {
        return SubCategoryRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SubCategoryRequest subCategoryRequest = (SubCategoryRequest) target;
        SubCategory subCategory = subCategoryService.findBySubCategoryName(subCategoryRequest.getSubCategoryName().trim().toLowerCase());
        if (subCategory != null) {
            errors.rejectValue("subCategoryName", "Sub Category has already existed.");
        }
    }
}
