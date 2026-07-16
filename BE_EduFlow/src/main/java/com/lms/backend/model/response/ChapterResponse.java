package com.lms.backend.model.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ChapterResponse {
    private Long chapterId;
    private String title;
    private String description;
    private List<LessonResponse> lessons;
    private boolean status;
}
