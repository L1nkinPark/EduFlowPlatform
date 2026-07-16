package com.lms.backend.service;

import com.lms.backend.model.entity.Account;
import java.util.List;

public interface LessonProgressService {
    boolean toggleLessonProgress(Account account, Long lessonId);
    List<Long> getCompletedLessonIds(Account account, String courseId);
    int getCourseProgressPercentage(Account account, String courseId);
}
