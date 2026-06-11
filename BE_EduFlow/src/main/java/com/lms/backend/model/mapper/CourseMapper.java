package com.lms.backend.model.mapper;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.entity.Course;

import com.lms.backend.model.response.AccountResponse;
import com.lms.backend.model.response.CourseResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CourseMapper {
    public CourseResponse convertToDTO(Course course) {
        if(course == null) {
            return null;
        }

        // Khoá Học
        CourseResponse courseResponse = new CourseResponse();
        courseResponse.setCourseId(course.getCourseId());
        courseResponse.setCourseName(course.getCourseName());
        courseResponse.setDescription(course.getDescription());
        courseResponse.setStartDate(course.getStartDate());
        courseResponse.setEndDate(course.getEndDate());
        courseResponse.setPrice(course.getPrice());
        courseResponse.setStatus(course.getStatus());
        courseResponse.setImage(course.getImage());
        courseResponse.setThumbnail(course.getThumbnail());
//        courseResponse.setCategory(course.getCategory());
//        courseResponse.setChapters(course.getChapters());

        // Tài Khoản
        Account account = course.getAccount();
        if (account != null) {
            AccountResponse accountResponse = new AccountResponse();
            accountResponse.setAccountId(account.getAccountId());
            accountResponse.setFullName(account.getFullName());

            courseResponse.setTeacher(accountResponse);
        }

        return courseResponse;
    }

    public List<CourseResponse> convertToDTO(List<Course> courseList) {
        if (courseList == null) {
            return null;
        }

        List<CourseResponse> courseResponseList = new ArrayList<>();
        for (Course course : courseList) {
            CourseResponse courseResponse = convertToDTO(course);
            courseResponseList.add(courseResponse);
        }
        return courseResponseList;
    }
}
