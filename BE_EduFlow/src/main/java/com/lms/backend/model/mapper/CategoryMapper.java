package com.lms.backend.model.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lms.backend.model.entity.Category;
import com.lms.backend.model.response.CategoryResponse;

@Component
public class CategoryMapper {
	
	public CategoryResponse convertToDTO(Category category) {
		if(category ==  null) {
			return null;
		}
		CategoryResponse categoryResponse = new CategoryResponse();
		categoryResponse.setCategoryId(category.getCategoryId());
		categoryResponse.setCategoryName(category.getCategoryName());
		categoryResponse.setCategoryDiscription(category.getCategoryDescription());
		categoryResponse.setStatus(category.isStatus());
		
		return categoryResponse;
	}
	
	public List<CategoryResponse> convertToDTO(List<Category> categoryList)
	{
		if(categoryList == null) {
			return null;
		}
		List <CategoryResponse> categoryResponseList = new ArrayList<>();
		for(Category category : categoryList) {
			CategoryResponse categoryRespose = convertToDTO(category);
			categoryResponseList.add(categoryRespose);
		}
		return categoryResponseList;
	}
}
