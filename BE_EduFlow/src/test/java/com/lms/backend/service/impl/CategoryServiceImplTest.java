package com.lms.backend.service.impl;

import com.lms.backend.exception.DataNotFoundException;
import com.lms.backend.model.entity.Category;
import com.lms.backend.model.request.CategoryRequest;
import com.lms.backend.repository.CategoryRepository;
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
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setCategoryId(1L);
        category.setCategoryName("Programming");
        category.setCategoryDescription("Learn Programming");
        category.setStatus(false);
    }

    @Test
    void testFindAllCategory() {
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));

        List<Category> result = categoryService.findAllCategory();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Programming", result.get(0).getCategoryName());
    }

    @Test
    void testGetAllCategory() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> page = new PageImpl<>(Collections.singletonList(category));
        when(categoryRepository.findAll(pageable)).thenReturn(page);

        Page<Category> result = categoryService.getAllCategory(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Programming", result.getContent().get(0).getCategoryName());
    }

    @Test
    void testSaveCategory_New() {
        CategoryRequest request = new CategoryRequest();
        request.setCategoryId(0L);
        request.setCategoryName("New Category");
        request.setCategoryDescription("New Description");
        request.setStatus(true);

        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        Category result = categoryService.saveCategory(request);

        assertNotNull(result);
        assertEquals("New Category", result.getCategoryName());
        assertTrue(result.isStatus());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testSaveCategory_Update_Success() {
        CategoryRequest request = new CategoryRequest();
        request.setCategoryId(1L);
        request.setCategoryName("Updated Name");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        Category result = categoryService.saveCategory(request);

        assertNotNull(result);
        assertEquals(1L, result.getCategoryId());
        assertEquals("Updated Name", result.getCategoryName());
    }

    @Test
    void testSaveCategory_Update_NotFound() {
        CategoryRequest request = new CategoryRequest();
        request.setCategoryId(2L);

        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> categoryService.saveCategory(request));
    }

    @Test
    void testFindById_Found() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getCategoryId());
    }

    @Test
    void testFindById_NotFound() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        Category result = categoryService.findById(2L);

        assertNull(result);
    }

    @Test
    void testDeleteById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        boolean result = categoryService.deleteById(1L);

        assertTrue(result);
        assertTrue(category.isStatus());
    }

    @Test
    void testDeleteById_NotFound() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        boolean result = categoryService.deleteById(2L);

        assertFalse(result);
    }

    @Test
    void testFindByCategoryName() {
        when(categoryRepository.findByCategoryName("Programming")).thenReturn(Optional.of(category));

        Category result = categoryService.findByCategoryName("Programming");

        assertNotNull(result);
        assertEquals("Programming", result.getCategoryName());
    }
}
