package com.lms.frontend.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InstructorStudentResponse {
    private Long accountId;
    private String fullName;
    private String email;
    private List<String> enrolledCourseNames;
}
