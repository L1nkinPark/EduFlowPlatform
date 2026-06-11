package com.lms.backend.controller;

import com.lms.backend.model.request.CategoryRequest;
import com.lms.backend.util.ValidatorUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.lms.backend.model.entity.Category;
import com.lms.backend.model.mapper.CategoryMapper;
import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CategoryMapper categoryMapper;
	
	@GetMapping
	public ResponseEntity<ApiResponse> getAllCategory(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10")Integer size)
	{
		Pageable pageable = PageRequest.of(currentPage -1 , size);
		Page<Category> categoryPage = categoryService.getAllCategory(pageable);
		
		ApiResponse response = new ApiResponse();
		
		response.ok();
		response.setPayload(categoryMapper.convertToDTO(categoryPage.getContent()));
		response.setPaginationMetadata(categoryPage.getTotalElements(), categoryPage.getTotalPages(), categoryPage.getNumber(),
		categoryPage.getSize());
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ApiResponse> saveCategory(@Valid @RequestBody CategoryRequest categoryRequest, BindingResult bindingResult){
		ApiResponse response= new ApiResponse();
		if (bindingResult.hasErrors()) {
			response.error("BAD_REQUEST", ValidatorUtil.toErrors((bindingResult.getFieldErrors())));
			return ResponseEntity.ok(response);
		}
		Category category = categoryService.saveCategory(categoryRequest);


		response.ok("OK", categoryMapper.convertToDTO(category));

		return ResponseEntity.ok(response);
	}

	@PutMapping("/{categoryId}")
	public ResponseEntity<ApiResponse> updateCategory(@Valid @RequestBody CategoryRequest categoryRequest, @PathVariable Long categoryId, BindingResult bindingResult){
		ApiResponse response = new ApiResponse();

		if (bindingResult.hasErrors()) {
			response.error("BAD_REQUEST", ValidatorUtil.toErrors(bindingResult.getFieldErrors()));
			return ResponseEntity.ok(response);
		}

		Category category = categoryService.saveCategory(categoryRequest);

		response.ok("OK", categoryMapper.convertToDTO(category));

		return ResponseEntity.ok(response);
	}

	@DeleteMapping(value = "{categoryId}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long categoryId){
		boolean temp = categoryService.deleteById(categoryId);
		ApiResponse response = new ApiResponse();
		if(temp){
			response.ok("OK");
			return ResponseEntity.ok(response);
		}
		response.error("BAD_REQUEST", null);
		return ResponseEntity.ok(response);
	}

}
