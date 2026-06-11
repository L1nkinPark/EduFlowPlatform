package com.lms.backend.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRequest {
//
    long accountId;
    String fullName;
    String username;
    String password;
    String email;
}
