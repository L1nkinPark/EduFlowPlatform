package com.lms.backend.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "lesson_progress", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"account_id", "lesson_id"})
}, indexes = {
    @Index(name = "idx_progress_account", columnList = "account_id"),
    @Index(name = "idx_progress_lesson", columnList = "lesson_id")
})
public class LessonProgress implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(name = "completed", nullable = false)
    private boolean completed;

    @Column(name = "completed_date")
    private LocalDate completedDate;
}
