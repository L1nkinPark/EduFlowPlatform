package com.lms.backend.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonResponse {
    private Long lessonId;
    private String title;
    private String video;
    private int duration;
    private boolean status;
}
