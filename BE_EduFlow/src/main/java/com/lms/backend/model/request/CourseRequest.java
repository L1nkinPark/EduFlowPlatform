package com.lms.backend.model.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class CourseRequest implements Serializable {
    private String courseId;
    private String courseName;
    private String description;
    private double price;
    private String status;
    private String image;
    private String thumbnail;
    private LocalDate startDate;
    private LocalDate endDate;

    //Category
//    private String subCategoryName;
}
