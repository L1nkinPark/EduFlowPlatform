package com.lms.backend.service.impl;

import com.lms.backend.exception.DataNotFoundException;
import com.lms.backend.model.entity.Chapter;
import com.lms.backend.model.entity.Lesson;
import com.lms.backend.model.request.LessonRequest;
import com.lms.backend.repository.ChapterRepository;
import com.lms.backend.repository.LessonRepository;
import com.lms.backend.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Override
    public Lesson createLesson(LessonRequest request) {
        Chapter chapter = chapterRepository.findById(request.getChapterId())
                .orElseThrow(() -> new DataNotFoundException("Chapter not found: " + request.getChapterId()));

        Lesson lesson = new Lesson();
        lesson.setTitle(request.getTitle());
        lesson.setLessonType(request.getLessonType() != null ? request.getLessonType() : "VIDEO");
        lesson.setVideo(request.getVideo());
        lesson.setContent(request.getContent());
        lesson.setDuration(request.getDuration());
        lesson.setStatus(request.isStatus());
        lesson.setChapter(chapter);

        return lessonRepository.save(lesson);
    }
}
