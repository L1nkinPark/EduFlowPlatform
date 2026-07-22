package com.lms.backend.service;

import com.lms.backend.model.entity.Chapter;
import com.lms.backend.model.request.ChapterRequest;

public interface ChapterService {
    Chapter createChapter(ChapterRequest request);
}
