package com.lms.frontend.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonResponse {
    private Long lessonId;

    private String title;

    // VIDEO or DOCUMENT
    private String lessonType;

    private String video;

    private String content;

    private String documentUrl;

    private String documentName;

    private String documentContentType;

    private int duration;

    private ChapterResponse chapter;

    private boolean status;
}
