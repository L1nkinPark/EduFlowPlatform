package com.lms.backend.model.mapper;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.entity.Course;

import com.lms.backend.model.response.AccountResponse;
import com.lms.backend.model.response.CourseResponse;
import com.lms.backend.model.response.ChapterResponse;
import com.lms.backend.model.response.LessonResponse;
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

        // Mapping chapters
        if (course.getChapters() != null) {
            List<ChapterResponse> chapterResponses = new ArrayList<>();
            for (com.lms.backend.model.entity.Chapter chapter : course.getChapters()) {
                ChapterResponse chapterResponse = new ChapterResponse();
                chapterResponse.setChapterId(chapter.getChapterId());
                chapterResponse.setTitle(chapter.getTitle());
                chapterResponse.setDescription(chapter.getDescription());
                chapterResponse.setStatus(chapter.isStatus());
                
                if (chapter.getLessons() != null) {
                    List<LessonResponse> lessonResponses = new ArrayList<>();
                    for (com.lms.backend.model.entity.Lesson lesson : chapter.getLessons()) {
                        LessonResponse lessonResponse = new LessonResponse();
                        lessonResponse.setLessonId(lesson.getLessonId());
                        lessonResponse.setTitle(lesson.getTitle());
                        lessonResponse.setVideo(lesson.getVideo());
                        lessonResponse.setDuration(lesson.getDuration());
                        lessonResponse.setStatus(lesson.isStatus());
                        lessonResponses.add(lessonResponse);
                    }
                    chapterResponse.setLessons(lessonResponses);
                }
                chapterResponses.add(chapterResponse);
            }
            courseResponse.setChapters(chapterResponses);
        }

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
