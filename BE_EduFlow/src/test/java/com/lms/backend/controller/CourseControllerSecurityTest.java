package com.lms.backend.controller;

import com.lms.backend.exception.ForbiddenException;
import com.lms.backend.exception.UnauthorizedException;
import com.lms.backend.model.entity.Account;
import com.lms.backend.security.CustomUserDetails;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CourseControllerSecurityTest {

    @Test
    void studentCannotListInstructorOwnedCourses() {
        CourseController controller = new CourseController();
        Account student = new Account();
        student.setRole("STUDENT");
        student.setUsername("student");

        assertThrows(ForbiddenException.class,
                () -> controller.getMyCourses(new CustomUserDetails(student)));
    }

    @Test
    void anonymousCallerCannotListInstructorOwnedCourses() {
        CourseController controller = new CourseController();

        assertThrows(UnauthorizedException.class,
                () -> controller.getMyCourses(null));
    }
}
