package com.lms.backend.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class AccountResponse implements Serializable {
    private Long accountId;
    private String fullName;
    private String email;
    private String phone;
    private String userName;
    private LocalDate birthday;
    private String role;
    private boolean status;
}
