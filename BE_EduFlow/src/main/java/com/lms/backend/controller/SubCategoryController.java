package com.lms.backend.controller;

import com.lms.backend.model.entity.SubCategory;
import com.lms.backend.model.mapper.SubCategoryMapper;
import com.lms.backend.model.request.SubCategoryRequest;
import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.service.CategoryService;
import com.lms.backend.service.SubCategoryService;
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

@RestController
@RequestMapping(value = "/api/subcategories")
public class SubCategoryController {
    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private SubCategoryMapper subCategoryMapper;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllSubCategory(@RequestParam(defaultValue = "1") Integer currentPage,@RequestParam(defaultValue = "10")Integer size){
        Pageable pageable = PageRequest.of(currentPage - 1, size);
        Page<SubCategory> subCategoryPage = subCategoryService.getAllSubCategory(pageable);

        ApiResponse response = new ApiResponse();
         response.ok();
         response.setPayload(subCategoryMapper.convertToDTO(subCategoryPage.getContent()));
         response.setPaginationMetadata(subCategoryPage.getTotalElements(), subCategoryPage.getTotalPages(), subCategoryPage.getNumber(), subCategoryPage.getSize());

         return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> saveSUbCategory(@Valid @RequestBody SubCategoryRequest subCategoryRequest, BindingResult bindingResult){
        ApiResponse response = new ApiResponse();
        if (bindingResult.hasErrors()) {
            response.error("BAD_REQUEST", ValidatorUtil.toErrors(bindingResult.getFieldErrors()));
            return ResponseEntity.ok(response);
        }
        SubCategory subCategory = subCategoryService.saveSubCategory(subCategoryRequest);


        response.ok("OK", subCategoryMapper.convertToDTO(subCategory));

        return  ResponseEntity.ok(response);
    }

    @PutMapping("/{subCategoryId}")
    public ResponseEntity<ApiResponse> updateSubCategory(@Valid @RequestBody SubCategoryRequest subCategoryRequest, @PathVariable Long subCategoryId, BindingResult bindingResult){

        ApiResponse response = new ApiResponse();

        if (bindingResult.hasErrors()) {
            response.error("BAD_REQUEST", ValidatorUtil.toErrors(bindingResult.getFieldErrors()));
            return ResponseEntity.ok(response);
        }
        SubCategory subCategory = subCategoryService.saveSubCategory(subCategoryRequest);

        response.ok("OK", subCategoryMapper.convertToDTO(subCategory));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "{subCategoryId}")
    public ResponseEntity<ApiResponse> deleteSubCategory(@PathVariable Long subCategoryId){
        boolean temp = subCategoryService.deleteById(subCategoryId);
        ApiResponse response = new ApiResponse();
        if(temp){
            response.ok("OK");
            return  ResponseEntity.ok(response);
        }
        response.error("BAD_REQUEST", null);
        return ResponseEntity.ok(response);
    }

}
