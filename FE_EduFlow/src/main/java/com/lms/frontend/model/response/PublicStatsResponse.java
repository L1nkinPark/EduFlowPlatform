package com.lms.frontend.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicStatsResponse {
    private long totalCourses;
    private long totalInstructors;
    private long totalStudents;
    private long totalEnrollments;
}
