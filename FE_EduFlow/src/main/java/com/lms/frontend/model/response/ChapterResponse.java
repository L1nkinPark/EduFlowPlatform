package com.lms.frontend.model.response;

import java.util.List;

public class ChapterResponse {
    private Long chapterId;

    private String title;

    private String description;

    private CourseResponse course;

    private ApiResponse<List<LessonResponse>> lessons;

    private boolean status;
}
