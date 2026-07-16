package com.lms.backend.repository;

import com.lms.backend.model.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.chapter.course.courseId = :courseId")
    long countTotalLessonsByCourseId(@Param("courseId") String courseId);
}
