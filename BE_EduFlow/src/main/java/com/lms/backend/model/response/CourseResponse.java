package com.lms.backend.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class CourseResponse {

    private String courseId;

    private String courseName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

    private double price;

    private String status;

    private String image;

    private String thumbnail;

    private java.util.List<ChapterResponse> chapters;

    private AccountResponse teacher;

    //Sub Category
    private SubCategoryResponse category;

}
