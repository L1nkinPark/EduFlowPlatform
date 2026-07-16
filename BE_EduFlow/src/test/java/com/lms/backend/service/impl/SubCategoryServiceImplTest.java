package com.lms.backend.service.impl;

import com.lms.backend.exception.DataNotFoundException;
import com.lms.backend.model.entity.Category;
import com.lms.backend.model.entity.SubCategory;
import com.lms.backend.model.request.SubCategoryRequest;
import com.lms.backend.repository.SubCategoryRepository;
import com.lms.backend.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubCategoryServiceImplTest {

    @Mock
    private SubCategoryRepository subCategoryRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private SubCategoryServiceImpl subCategoryService;

    private SubCategory subCategory;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setCategoryId(10L);
        category.setCategoryName("Tech");

        subCategory = new SubCategory();
        subCategory.setSubCategoryId(1L);
        subCategory.setSubCategoryName("Java");
        subCategory.setSubCategoryDescription("Java Programming");
        subCategory.setStatus(false);
        subCategory.setCategory(category);
    }

    @Test
    void testFindAllSubCategory() {
        when(subCategoryRepository.findAll()).thenReturn(Collections.singletonList(subCategory));

        List<SubCategory> result = subCategoryService.findAllSubCategory();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getSubCategoryName());
    }

    @Test
    void testGetAllSubCategory() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SubCategory> page = new PageImpl<>(Collections.singletonList(subCategory));
        when(subCategoryRepository.findAll(pageable)).thenReturn(page);

        Page<SubCategory> result = subCategoryService.getAllSubCategory(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Java", result.getContent().get(0).getSubCategoryName());
    }

    @Test
    void testSaveSubCategory_New() {
        SubCategoryRequest request = new SubCategoryRequest();
        request.setSubCategoryId(0L);
        request.setSubCategoryName("Python");
        request.setSubCategoryDescription("Python Programming");
        request.setStatus(true);
        request.setCategoryId(10L);

        when(categoryService.findById(10L)).thenReturn(category);
        when(subCategoryRepository.save(any(SubCategory.class))).thenAnswer(inv -> inv.getArgument(0));

        SubCategory result = subCategoryService.saveSubCategory(request);

        assertNotNull(result);
        assertEquals("Python", result.getSubCategoryName());
        assertEquals(category, result.getCategory());
        assertTrue(result.isStatus());
        verify(subCategoryRepository, times(1)).save(any(SubCategory.class));
    }

    @Test
    void testSaveSubCategory_Update_Success() {
        SubCategoryRequest request = new SubCategoryRequest();
        request.setSubCategoryId(1L);
        request.setSubCategoryName("Java EE");
        request.setCategoryId(10L);

        when(subCategoryRepository.findById(1L)).thenReturn(Optional.of(subCategory));
        when(categoryService.findById(10L)).thenReturn(category);
        when(subCategoryRepository.save(any(SubCategory.class))).thenAnswer(inv -> inv.getArgument(0));

        SubCategory result = subCategoryService.saveSubCategory(request);

        assertNotNull(result);
        assertEquals(1L, result.getSubCategoryId());
        assertEquals("Java EE", result.getSubCategoryName());
    }

    @Test
    void testSaveSubCategory_Update_NotFound() {
        SubCategoryRequest request = new SubCategoryRequest();
        request.setSubCategoryId(2L);

        when(subCategoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> subCategoryService.saveSubCategory(request));
    }

    @Test
    void testFindById_Found() {
        when(subCategoryRepository.findById(1L)).thenReturn(Optional.of(subCategory));

        SubCategory result = subCategoryService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getSubCategoryId());
    }

    @Test
    void testFindById_NotFound() {
        when(subCategoryRepository.findById(2L)).thenReturn(Optional.empty());

        SubCategory result = subCategoryService.findById(2L);

        assertNull(result);
    }

    @Test
    void testDeleteById_Success() {
        when(subCategoryRepository.findById(1L)).thenReturn(Optional.of(subCategory));
        when(subCategoryRepository.save(any(SubCategory.class))).thenAnswer(inv -> inv.getArgument(0));

        boolean result = subCategoryService.deleteById(1L);

        assertTrue(result);
        assertTrue(subCategory.isStatus());
    }

    @Test
    void testDeleteById_NotFound() {
        when(subCategoryRepository.findById(2L)).thenReturn(Optional.empty());

        boolean result = subCategoryService.deleteById(2L);

        assertFalse(result);
    }

    @Test
    void testFindBySubCategoryName() {
        when(subCategoryRepository.findBySubCategoryName("Java")).thenReturn(Optional.of(subCategory));

        SubCategory result = subCategoryService.findBySubCategoryName("Java");

        assertNotNull(result);
        assertEquals("Java", result.getSubCategoryName());
    }
}
