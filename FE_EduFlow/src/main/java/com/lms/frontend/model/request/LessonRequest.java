package com.lms.frontend.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonRequest {
    private Long chapterId;
    private String title;
    // VIDEO or DOCUMENT
    private String lessonType = "VIDEO";
    private String video;
    private String content;
    private int duration;
    private boolean status = true;
}
