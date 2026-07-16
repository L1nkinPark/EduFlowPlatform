package com.lms.backend.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "lessons", indexes = {
    @Index(name = "idx_lessons_chapter", columnList = "chapter_id")
})
public class Lesson implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "video")
    private String video;

    @Column(name = "duration")
    private int duration;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @Column(name = "status")
    private boolean status;

}

