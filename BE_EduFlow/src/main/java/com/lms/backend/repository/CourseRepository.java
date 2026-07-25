package com.lms.backend.repository;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    @Query("SELECT c FROM Course c WHERE c.courseName LIKE %?1%")
    Page<Course> searchCourse(Pageable pageable, String keyword);

    @Query("""
            SELECT c FROM Course c
            LEFT JOIN c.subCategory sc
            LEFT JOIN sc.category category
            WHERE (:keyword IS NULL OR LOWER(c.courseName) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:categoryId IS NULL OR category.categoryId = :categoryId)
              AND (:subCategoryId IS NULL OR sc.subCategoryId = :subCategoryId)
            """)
    Page<Course> filterCourses(Pageable pageable,
                               @Param("keyword") String keyword,
                               @Param("categoryId") Long categoryId,
                               @Param("subCategoryId") Long subCategoryId);

    List<Course> findByAccount(Account account);

}
