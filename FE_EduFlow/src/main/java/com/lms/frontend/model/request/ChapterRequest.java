package com.lms.frontend.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChapterRequest {
    private String courseId;
    private String title;
    private String description;
    private boolean status = true;
}
