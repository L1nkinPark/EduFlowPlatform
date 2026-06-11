package com.lms.backend.model.request;

import com.lms.backend.model.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubCategoryRequest {
    @NotNull(message = "ID is required")
    private Long subCategoryId;

    @NotBlank(message = "Sub Category name is required")
    private String subCategoryName;

    private String subCategoryDescription;
    private boolean status;
    @NotNull(message = "Category is required")
    private Long categoryId;
}
