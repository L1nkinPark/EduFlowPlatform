package com.lms.frontend.model.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileUpdateRequest {
    private String fullName;
    private String phone;
    private LocalDate birthday;
}
