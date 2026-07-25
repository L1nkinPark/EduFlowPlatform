package com.lms.backend.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactMessageRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    @Size(max = 160, message = "Email must not exceed 160 characters")
    private String email;

    @Pattern(regexp = "^$|^[0-9+() .-]{7,30}$", message = "Phone number is invalid")
    private String phone;

    @NotBlank(message = "Subject is required")
    @Size(max = 160, message = "Subject must not exceed 160 characters")
    private String subject;

    @NotBlank(message = "Message is required")
    @Size(min = 10, max = 5000, message = "Message must contain 10 to 5000 characters")
    private String message;
}
