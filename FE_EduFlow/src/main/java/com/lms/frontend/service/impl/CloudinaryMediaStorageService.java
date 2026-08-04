package com.lms.frontend.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lms.frontend.service.MediaStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class CloudinaryMediaStorageService implements MediaStorageService {

    private static final long IMAGE_MAX_BYTES = 5L * 1024 * 1024;
    private static final long DOCUMENT_MAX_BYTES = 25L * 1024 * 1024;
    private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp", "gif");
    private static final Set<String> DOCUMENT_EXTENSIONS = Set.of(
            "pdf", "doc", "docx", "ppt", "pptx", "xls", "xlsx", "txt", "rtf", "csv", "odt", "ods", "odp");

    private final Cloudinary cloudinary;

    @Value("${cloudinary.api_secret:}")
    private String cloudinaryApiSecret;

    @Value("${media.local-directory:./uploads}")
    private String localDirectory;

    public CloudinaryMediaStorageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public UploadedMedia uploadCourseImage(MultipartFile file) {
        validate(file, IMAGE_MAX_BYTES, IMAGE_EXTENSIONS, "ảnh", "5 MB");
        return upload(file, "image", "eduflow/course-covers", "course-covers");
    }

    @Override
    public UploadedMedia uploadLessonDocument(MultipartFile file) {
        validate(file, DOCUMENT_MAX_BYTES, DOCUMENT_EXTENSIONS, "tài liệu", "25 MB");
        if ("pdf".equals(extensionOf(safeOriginalName(file)))) {
            validatePdfSignature(file);
        }
        return upload(file, "raw", "eduflow/lesson-documents", "lesson-documents");
    }

    private UploadedMedia upload(MultipartFile file, String resourceType, String cloudFolder,
                                 String localFolder) {
        if (cloudinaryApiSecret == null || cloudinaryApiSecret.isBlank()) {
            return storeLocally(file, localFolder);
        }
        try {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", resourceType,
                    "folder", cloudFolder,
                    "use_filename", true,
                    "unique_filename", true,
                    "overwrite", false));
            Object secureUrl = result.get("secure_url");
            if (secureUrl == null || secureUrl.toString().isBlank()) {
                throw new IllegalStateException("Dịch vụ lưu trữ không trả về đường dẫn tệp.");
            }
            return new UploadedMedia(secureUrl.toString(), safeOriginalName(file), file.getContentType());
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalStateException("Không thể tải tệp lên. Vui lòng thử lại hoặc kiểm tra cấu hình lưu trữ.", ex);
        }
    }

    private UploadedMedia storeLocally(MultipartFile file, String folder) {
        try {
            String originalName = safeOriginalName(file);
            String extension = extensionOf(originalName);
            String storedName = UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);
            Path root = Paths.get(localDirectory).toAbsolutePath().normalize();
            Path targetDirectory = root.resolve(folder).normalize();
            if (!targetDirectory.startsWith(root)) {
                throw new IllegalStateException("Thư mục lưu tệp không hợp lệ.");
            }
            Files.createDirectories(targetDirectory);
            Path target = targetDirectory.resolve(storedName);
            try (java.io.InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, target);
            }
            return new UploadedMedia("/uploads/" + folder + "/" + storedName,
                    originalName, file.getContentType());
        } catch (IllegalArgumentException | IllegalStateException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalStateException("Không thể lưu tệp trên máy chủ. Vui lòng thử lại.", ex);
        }
    }

    private void validate(MultipartFile file, long maxBytes, Set<String> extensions,
                          String mediaLabel, String displayLimit) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn " + mediaLabel + " để tải lên.");
        }
        if (file.getSize() > maxBytes) {
            throw new IllegalArgumentException("Tệp " + mediaLabel + " không được vượt quá " + displayLimit + ".");
        }
        String extension = extensionOf(safeOriginalName(file));
        if (!extensions.contains(extension)) {
            throw new IllegalArgumentException("Định dạng " + mediaLabel + " không được hỗ trợ.");
        }
    }

    private void validatePdfSignature(MultipartFile file) {
        byte[] expectedSignature = {'%', 'P', 'D', 'F', '-'};
        try (InputStream inputStream = file.getInputStream()) {
            byte[] actualSignature = inputStream.readNBytes(expectedSignature.length);
            if (actualSignature.length != expectedSignature.length) {
                throw new IllegalArgumentException("Tệp đã chọn không phải PDF hợp lệ.");
            }
            for (int index = 0; index < expectedSignature.length; index++) {
                if (actualSignature[index] != expectedSignature[index]) {
                    throw new IllegalArgumentException("Tệp đã chọn không phải PDF hợp lệ.");
                }
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException("Không thể đọc tệp PDF đã chọn.", ex);
        }
    }

    private String safeOriginalName(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.isBlank()) {
            return "file";
        }
        String normalized = originalName.replace('\\', '/');
        String baseName = normalized.substring(normalized.lastIndexOf('/') + 1).trim();
        return baseName.length() > 255 ? baseName.substring(baseName.length() - 255) : baseName;
    }

    private String extensionOf(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return dot < 0 ? "" : fileName.substring(dot + 1).toLowerCase(Locale.ROOT);
    }
}
