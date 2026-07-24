package com.lms.frontend.service;

import com.lms.frontend.model.request.ChapterRequest;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.ChapterResponse;

public interface ChapterService {
    ApiResponse<ChapterResponse> createChapter(ChapterRequest request);
}
