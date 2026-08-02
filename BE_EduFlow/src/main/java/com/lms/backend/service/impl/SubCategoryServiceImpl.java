package com.lms.backend.service.impl;

import java.util.List;

import com.lms.backend.exception.DataNotFoundException;
import com.lms.backend.model.entity.Category;
import com.lms.backend.model.request.SubCategoryRequest;
import com.lms.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lms.backend.model.entity.SubCategory;
import com.lms.backend.repository.SubCategoryRepository;
import com.lms.backend.service.SubCategoryService;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Override
    @org.springframework.cache.annotation.Cacheable("subcategories")
    public List<SubCategory> findAllSubCategory() {
        return subCategoryRepository.findAll();
    }

    @Override
    @org.springframework.cache.annotation.Cacheable(value = "subcategories", key = "#pageable")
    public Page<SubCategory> getAllSubCategory(Pageable pageable) {
        return subCategoryRepository.findAll(pageable);
    }

    @Override
    @org.springframework.cache.annotation.CacheEvict(value = "subcategories", allEntries = true)
    public SubCategory saveSubCategory(SubCategoryRequest subCategoryRequest) {
        if (subCategoryRequest == null) {
            throw new IllegalArgumentException("Subcategory request is required");
        }
        SubCategory subCategory;
        if (subCategoryRequest.getSubCategoryId() == 0) {
            subCategory = new SubCategory();
        } else {
            subCategory = findById(subCategoryRequest.getSubCategoryId());
        }
        subCategory.setSubCategoryId(subCategoryRequest.getSubCategoryId());
        subCategory.setSubCategoryName(subCategoryRequest.getSubCategoryName());
        subCategory.setSubCategoryDescription(subCategoryRequest.getSubCategoryDescription());
        subCategory.setStatus(subCategoryRequest.isStatus());
        Category category = categoryService.findById(subCategoryRequest.getCategoryId());
        subCategory.setCategory(category);
        return subCategoryRepository.save(subCategory);
    }

    @Override
    @org.springframework.cache.annotation.Cacheable(value = "subcategories", key = "#subCategory")
    public SubCategory findById(Long subCategory) {
        return subCategoryRepository.findById(subCategory)
                .orElseThrow(() -> new DataNotFoundException("Subcategory not found: " + subCategory));
    }

    @Override
    @org.springframework.cache.annotation.CacheEvict(value = "subcategories", allEntries = true)
    public boolean deleteById(Long subCategoryId) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElse(null);
        if (subCategory != null) {
            subCategory.setStatus(true);
            subCategoryRepository.save(subCategory);
            return true;
        }
        return false;
    }

    @Override
    @org.springframework.cache.annotation.Cacheable(value = "subcategories", key = "#name")
    public SubCategory findBySubCategoryName(String name) {
        return subCategoryRepository.findBySubCategoryName(name).orElse(null);
    }
}
