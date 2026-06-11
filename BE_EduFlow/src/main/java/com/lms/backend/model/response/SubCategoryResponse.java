package com.lms.backend.model.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubCategoryResponse {
    private Long subCategoryId;
    private String subCategoryName;
    private String subCategoryDescription;
    private boolean status;
    private CategoryResponse category;


}
