package com.lms.backend.service.impl;

import com.lms.backend.exception.DataNotFoundException;
import com.lms.backend.model.entity.Chapter;
import com.lms.backend.model.entity.Course;
import com.lms.backend.model.request.ChapterRequest;
import com.lms.backend.repository.ChapterRepository;
import com.lms.backend.repository.CourseRepository;
import com.lms.backend.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChapterServiceImpl implements ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Chapter createChapter(ChapterRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new DataNotFoundException("Course not found: " + request.getCourseId()));

        Chapter chapter = new Chapter();
        chapter.setTitle(request.getTitle());
        chapter.setDescription(request.getDescription());
        chapter.setStatus(request.isStatus());
        chapter.setCourse(course);

        return chapterRepository.save(chapter);
    }
}
