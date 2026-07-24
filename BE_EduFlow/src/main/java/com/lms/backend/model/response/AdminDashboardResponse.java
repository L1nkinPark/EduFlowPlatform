package com.lms.backend.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AdminDashboardResponse implements Serializable {
    private long totalStudents;
    private long totalInstructors;
    private long totalAdmins;
    private long totalCourses;
    private long totalOrders;
    private double totalRevenue;
}
