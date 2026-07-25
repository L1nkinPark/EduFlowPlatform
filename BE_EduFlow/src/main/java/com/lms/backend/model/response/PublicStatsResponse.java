package com.lms.backend.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

// Số liệu tổng quan công khai của nền tảng (dùng cho trang About/Landing),
// khác với AdminDashboardResponse vì không yêu cầu quyền ADMIN và không lộ doanh thu.
@Getter
@Setter
public class PublicStatsResponse implements Serializable {
    private long totalCourses;
    private long totalInstructors;
    private long totalStudents;
    private long totalEnrollments;
}
