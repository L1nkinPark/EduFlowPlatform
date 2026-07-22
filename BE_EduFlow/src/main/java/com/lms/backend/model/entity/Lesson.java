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

    // VIDEO or DOCUMENT. VIDEO lessons play the `video` field; DOCUMENT
    // lessons render the `content` field as reading material.
    @Column(name = "lesson_type")
    private String lessonType = "VIDEO";

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "duration")
    private int duration;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @Column(name = "status")
    private boolean status;

}

