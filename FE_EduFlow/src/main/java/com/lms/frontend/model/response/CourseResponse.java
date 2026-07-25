package com.lms.frontend.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class CourseResponse {
//
    private String courseId;

    private String courseName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

    private double price;

    private String status;

    private Long subCategoryId;

    // Kept as "category" for API compatibility; this object represents the
    // selected subcategory and contains its parent category.
    private SubCategoryResponse category;

    private String image;

    private String thumbnail;

//    private List<Chapter> chapters = new ArrayList<>();

    private AccountResponse teacher;

    private java.util.List<ChapterResponse> chapters;

    // Số học viên thực đã mua khóa học này.
    private long enrollmentCount;
}
