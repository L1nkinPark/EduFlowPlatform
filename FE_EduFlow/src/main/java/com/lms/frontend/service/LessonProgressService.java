package com.lms.frontend.service;

import com.lms.frontend.model.response.ApiResponse;
import java.util.List;

public interface LessonProgressService {
    ApiResponse<Boolean> toggleProgress(Long lessonId);
    ApiResponse<List<Long>> getCompletedLessonIds(String courseId);
    ApiResponse<Integer> getCourseProgressPercentage(String courseId);
}
