package com.lms.backend.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class InstructorDashboardResponse implements Serializable {
    private long totalCourses;
    private long totalStudents;
    private long totalSales;
    private double totalRevenue;
}
