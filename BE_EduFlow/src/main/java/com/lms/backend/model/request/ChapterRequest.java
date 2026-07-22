package com.lms.backend.model.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChapterRequest implements Serializable {
    private String courseId;
    private String title;
    private String description;
    private boolean status = true;
}
