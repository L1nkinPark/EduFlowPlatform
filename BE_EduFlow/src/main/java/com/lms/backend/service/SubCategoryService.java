package com.lms.backend.service;

import java.util.List;

import com.lms.backend.model.entity.SubCategory;
import com.lms.backend.model.request.SubCategoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubCategoryService {
	List<SubCategory> findAllSubCategory();

	Page<SubCategory> getAllSubCategory(Pageable pageable);
	
	SubCategory saveSubCategory(SubCategoryRequest subCategoryRequest);
	
	SubCategory findById(Long subCategory);
	
	boolean deleteById(Long subCategoryId);

	SubCategory findBySubCategoryName(String name);
}
