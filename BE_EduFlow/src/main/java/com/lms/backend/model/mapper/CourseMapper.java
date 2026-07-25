package com.lms.backend.model.mapper;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.entity.Course;

import com.lms.backend.model.response.AccountResponse;
import com.lms.backend.model.response.CourseResponse;
import com.lms.backend.model.response.ChapterResponse;
import com.lms.backend.model.response.LessonResponse;
import com.lms.backend.model.response.SubCategoryResponse;
import com.lms.backend.model.response.CategoryResponse;
import com.lms.backend.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CourseMapper {

    @Autowired
    private OrderItemRepository orderItemRepository;

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
        courseResponse.setEnrollmentCount(orderItemRepository.countByCourse(course));

        if (course.getSubCategory() != null) {
            courseResponse.setSubCategoryId(course.getSubCategory().getSubCategoryId());
            SubCategoryResponse subCategoryResponse = new SubCategoryResponse();
            subCategoryResponse.setSubCategoryId(course.getSubCategory().getSubCategoryId());
            subCategoryResponse.setSubCategoryName(course.getSubCategory().getSubCategoryName());
            subCategoryResponse.setSubCategoryDescription(course.getSubCategory().getSubCategoryDescription());

            if (course.getSubCategory().getCategory() != null) {
                CategoryResponse categoryResponse = new CategoryResponse();
                categoryResponse.setCategoryId(course.getSubCategory().getCategory().getCategoryId());
                categoryResponse.setCategoryName(course.getSubCategory().getCategory().getCategoryName());
                subCategoryResponse.setCategory(categoryResponse);
            }
            courseResponse.setCategory(subCategoryResponse);
        }

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
                        lessonResponse.setLessonType(lesson.getLessonType());
                        lessonResponse.setVideo(lesson.getVideo());
                        lessonResponse.setContent(lesson.getContent());
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
