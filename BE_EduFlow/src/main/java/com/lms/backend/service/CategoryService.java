package com.lms.backend.service;

import java.util.List;

import com.lms.backend.model.request.CategoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lms.backend.model.entity.Category;

public interface CategoryService {
	List<Category> findAllCategory();
	
	Page<Category> getAllCategory(Pageable pageable);
	
	Category saveCategory(CategoryRequest categoryRequest);
	
	Category findById(Long categoryId);
	
	boolean deleteById(Long categoryId);

	Category findByCategoryName(String name);
}
