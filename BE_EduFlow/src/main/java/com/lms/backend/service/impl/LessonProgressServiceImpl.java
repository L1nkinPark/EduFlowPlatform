package com.lms.backend.service.impl;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.entity.Lesson;
import com.lms.backend.model.entity.LessonProgress;
import com.lms.backend.repository.LessonProgressRepository;
import com.lms.backend.repository.LessonRepository;
import com.lms.backend.service.LessonProgressService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LessonProgressServiceImpl implements LessonProgressService {

    @Autowired
    private LessonProgressRepository lessonProgressRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Transactional
    @Override
    public boolean toggleLessonProgress(Account account, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with ID: " + lessonId));

        LessonProgress progress = lessonProgressRepository.findByAccountAndLesson(account, lesson)
                .orElse(null);

        if (progress == null) {
            try {
                progress = new LessonProgress();
                progress.setAccount(account);
                progress.setLesson(lesson);
                progress.setCompleted(true);
                progress.setCompletedDate(LocalDate.now());
                lessonProgressRepository.saveAndFlush(progress);
                return true;
            } catch (Exception ex) {
                return true;
            }
        } else {
            boolean nextState = !progress.isCompleted();
            progress.setCompleted(nextState);
            progress.setCompletedDate(nextState ? LocalDate.now() : null);
            lessonProgressRepository.save(progress);
            return nextState;
        }
    }

    @Override
    public List<Long> getCompletedLessonIds(Account account, String courseId) {
        return lessonProgressRepository.findCompletedLessonIdsByAccountAndCourseId(account, courseId);
    }

    @Override
    public int getCourseProgressPercentage(Account account, String courseId) {
        long totalLessons = lessonRepository.countTotalLessonsByCourseId(courseId);
        if (totalLessons == 0) {
            return 0;
        }
        long completedLessons = lessonProgressRepository.countCompletedLessonsByAccountAndCourseId(account, courseId);
        return (int) Math.round((double) completedLessons * 100.0 / (double) totalLessons);
    }
}
