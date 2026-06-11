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

//    private SubCategory category;

    private String image;

    private String thumbnail;

//    private List<Chapter> chapters = new ArrayList<>();

    private AccountResponse teacher;


}