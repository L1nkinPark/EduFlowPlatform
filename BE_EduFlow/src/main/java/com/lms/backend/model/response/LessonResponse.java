package com.lms.backend.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonResponse {
    private Long lessonId;
    private String title;
    private String lessonType;
    private String video;
    private String content;
    private int duration;
    private boolean status;
}
