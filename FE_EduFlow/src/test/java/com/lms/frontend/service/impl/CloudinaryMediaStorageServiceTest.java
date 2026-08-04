package com.lms.frontend.service.impl;

import com.cloudinary.Cloudinary;
import com.lms.frontend.service.MediaStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CloudinaryMediaStorageServiceTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void storesPdfLocallyWhenCloudinaryIsNotConfigured() throws Exception {
        CloudinaryMediaStorageService service = new CloudinaryMediaStorageService(new Cloudinary());
        ReflectionTestUtils.setField(service, "cloudinaryApiSecret", "");
        ReflectionTestUtils.setField(service, "localDirectory", temporaryDirectory.toString());
        MockMultipartFile file = new MockMultipartFile(
                "lessonFile", "tai-lieu.pdf", "application/pdf", "%PDF-test".getBytes());

        MediaStorageService.UploadedMedia uploaded = service.uploadLessonDocument(file);

        assertEquals("tai-lieu.pdf", uploaded.originalName());
        assertEquals("application/pdf", uploaded.contentType());
        assertTrue(uploaded.url().startsWith("/uploads/lesson-documents/"));
        String storedName = uploaded.url().substring(uploaded.url().lastIndexOf('/') + 1);
        assertTrue(Files.exists(temporaryDirectory.resolve("lesson-documents").resolve(storedName)));
    }

    @Test
    void rejectsHtmlFileRenamedToPdf() {
        CloudinaryMediaStorageService service = new CloudinaryMediaStorageService(new Cloudinary());
        ReflectionTestUtils.setField(service, "cloudinaryApiSecret", "");
        ReflectionTestUtils.setField(service, "localDirectory", temporaryDirectory.toString());
        MockMultipartFile file = new MockMultipartFile(
                "lessonFile", "tai-lieu.pdf", "application/pdf", "<!doctype html>".getBytes());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> service.uploadLessonDocument(file));

        assertEquals("Tệp đã chọn không phải PDF hợp lệ.", exception.getMessage());
        assertFalse(Files.exists(temporaryDirectory.resolve("lesson-documents")));
    }
}
