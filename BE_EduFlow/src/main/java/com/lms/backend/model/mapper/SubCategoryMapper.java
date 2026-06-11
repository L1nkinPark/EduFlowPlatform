package com.lms.backend.model.mapper;

import com.lms.backend.model.entity.Category;
import com.lms.backend.model.entity.SubCategory;
import com.lms.backend.model.response.CategoryResponse;
import com.lms.backend.model.response.SubCategoryResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class SubCategoryMapper {
    public SubCategoryResponse convertToDTO(SubCategory subCategory){
        if(subCategory == null){
            return null;
        }

        SubCategoryResponse subCategoryResponse = new SubCategoryResponse();
        subCategoryResponse.setSubCategoryName(subCategory.getSubCategoryName());
        subCategoryResponse.setSubCategoryDescription(subCategory.getSubCategoryDescription());
        subCategoryResponse.setStatus(subCategory.isStatus());

        Category category = subCategory.getCategory();
        if(category != null){
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setCategoryId(category.getCategoryId());
            categoryResponse.setCategoryName(category.getCategoryName());

            subCategoryResponse.setCategory(categoryResponse);
        }
        return subCategoryResponse;
    }

    public List<SubCategoryResponse> convertToDTO(List<SubCategory> subCategoriesList){
        if(subCategoriesList == null){
            return null;
        }
        List<SubCategoryResponse> subCategoryResponseList = new ArrayList<>();
        for(SubCategory subCategory : subCategoriesList){
            SubCategoryResponse subCategoryResponse = convertToDTO(subCategory);
            subCategoryResponseList.add(subCategoryResponse);
        }
        return subCategoryResponseList;
    }
}
