package com.lms.backend.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class InstructorStudentResponse implements Serializable {
    private Long accountId;
    private String fullName;
    private String email;
    private List<String> enrolledCourseNames;
}
