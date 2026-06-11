package com.lms.backend.model.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CourseRequest implements Serializable {
    private String courseId;
    private String courseName;
    private String description;
    private double price;
    private String status;

    //Category
//    private String subCategoryName;
}
