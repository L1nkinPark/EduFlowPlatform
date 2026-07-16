package com.lms.backend.repository;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.entity.Lesson;
import com.lms.backend.model.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {

    Optional<LessonProgress> findByAccountAndLesson(Account account, Lesson lesson);

    @Query("SELECT lp.lesson.lessonId FROM LessonProgress lp WHERE lp.account = :account AND lp.lesson.chapter.course.courseId = :courseId AND lp.completed = true")
    List<Long> findCompletedLessonIdsByAccountAndCourseId(@Param("account") Account account, @Param("courseId") String courseId);

    @Query("SELECT COUNT(lp) FROM LessonProgress lp WHERE lp.account = :account AND lp.lesson.chapter.course.courseId = :courseId AND lp.completed = true")
    long countCompletedLessonsByAccountAndCourseId(@Param("account") Account account, @Param("courseId") String courseId);
}
