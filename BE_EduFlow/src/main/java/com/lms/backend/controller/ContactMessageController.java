package com.lms.backend.controller;

import com.lms.backend.model.entity.ContactMessage;
import com.lms.backend.model.request.ContactMessageRequest;
import com.lms.backend.model.response.ApiResponse;
import com.lms.backend.service.impl.ContactMessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/contact-messages")
public class ContactMessageController {

    @Autowired
    private ContactMessageService contactMessageService;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> submit(
            @Valid @RequestBody ContactMessageRequest request) {
        ContactMessage savedMessage = contactMessageService.submit(request);
        ApiResponse<Map<String, Object>> response = new ApiResponse<>();
        response.ok("Contact request received", Map.of(
                "referenceId", savedMessage.getContactMessageId(),
                "status", savedMessage.getStatus()
        ));
        return ResponseEntity.ok(response);
    }
}
