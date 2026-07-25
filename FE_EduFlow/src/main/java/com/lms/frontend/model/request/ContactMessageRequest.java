package com.lms.frontend.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactMessageRequest {

    @NotBlank(message = "{contact.validation.name}")
    @Size(max = 100, message = "{contact.validation.name_length}")
    private String fullName;

    @NotBlank(message = "{contact.validation.email_required}")
    @Email(message = "{contact.validation.email}")
    @Size(max = 160, message = "{contact.validation.email}")
    private String email;

    @Pattern(regexp = "^$|^[0-9+() .-]{7,30}$", message = "{contact.validation.phone}")
    private String phone;

    @NotBlank(message = "{contact.validation.subject}")
    @Size(max = 160, message = "{contact.validation.subject_length}")
    private String subject;

    @NotBlank(message = "{contact.validation.message}")
    @Size(min = 10, max = 5000, message = "{contact.validation.message_length}")
    private String message;

    private String website;
}
