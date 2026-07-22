package com.lms.backend.service;

import com.lms.backend.model.entity.Lesson;
import com.lms.backend.model.request.LessonRequest;

public interface LessonService {
    Lesson createLesson(LessonRequest request);
}
