package com.lms.frontend.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstructorDashboardResponse {
    private long totalCourses;
    private long totalStudents;
    private long totalSales;
    private double totalRevenue;
}
