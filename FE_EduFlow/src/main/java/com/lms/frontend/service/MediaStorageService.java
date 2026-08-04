package com.lms.frontend.service;

import org.springframework.web.multipart.MultipartFile;

public interface MediaStorageService {

    UploadedMedia uploadCourseImage(MultipartFile file);

    UploadedMedia uploadLessonDocument(MultipartFile file);

    record UploadedMedia(String url, String originalName, String contentType) {
    }
}
