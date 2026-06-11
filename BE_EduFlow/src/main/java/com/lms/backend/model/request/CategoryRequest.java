package com.lms.backend.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CategoryRequest implements Serializable {

    @NotNull(message = "ID is required")
    private Long categoryId;

    @NotBlank(message = "Category name is required")
    private String categoryName;
    private String categoryDescription;
    private boolean status;
}
