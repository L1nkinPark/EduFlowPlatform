package com.lms.backend.service.impl;

import java.util.List;
import java.util.Optional;

import com.lms.backend.exception.DataNotFoundException;
import com.lms.backend.model.request.CategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lms.backend.model.entity.Category;
import com.lms.backend.repository.CategoryRepository;
import com.lms.backend.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<Category> findAllCategory() {
		// TODO Auto-generated method stub
		return categoryRepository.findAll();
	}

	@Override
	public Page<Category> getAllCategory(Pageable pageable) {
		// TODO Auto-generated method stub
		return categoryRepository.findAll(pageable);
	}



	@Override
	public Category saveCategory(CategoryRequest categoryRequest) {
		Category category = null;
		if (categoryRequest == null || categoryRequest.getCategoryId() == 0) {
			category = new Category();
		} else {
			category = findById(categoryRequest.getCategoryId());
			if(category == null){
				throw new DataNotFoundException();
			}
		}
		category.setCategoryName(categoryRequest.getCategoryName());
		category.setCategoryDescription(categoryRequest.getCategoryDescription());
		category.setStatus(categoryRequest.isStatus());

		return categoryRepository.save(category);
	}

	@Override
	public Category findById(Long categoryId) {
		// TODO Auto-generated method stub
		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
		if (optionalCategory.isPresent()) {
			return optionalCategory.get();
		} else {
			return null;
		}
	}

	@Override
	public boolean deleteById(Long categoryId) {
		// TODO Auto-generated method stub
		Category category = categoryRepository.findById(categoryId).orElse(null);
		if(category != null) {
			category.setStatus(true);
			categoryRepository.save(category);
			return true;
		}
		return false;
	}

	@Override
	public Category findByCategoryName(String name) {
		return categoryRepository.findByCategoryName(name).orElse(null);
	}
}
