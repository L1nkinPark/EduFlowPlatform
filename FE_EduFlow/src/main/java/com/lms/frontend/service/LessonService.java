package com.lms.frontend.service;

import com.lms.frontend.model.request.LessonRequest;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.LessonResponse;

public interface LessonService {
    ApiResponse<LessonResponse> createLesson(LessonRequest request);
}
