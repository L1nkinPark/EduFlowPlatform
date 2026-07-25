package com.lms.backend.service.impl;

import com.lms.backend.model.entity.ContactMessage;
import com.lms.backend.model.request.ContactMessageRequest;
import com.lms.backend.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ContactMessageService {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${contact.notification-email:}")
    private String notificationEmail;

    @Transactional
    public ContactMessage submit(ContactMessageRequest request) {
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setFullName(request.getFullName().trim());
        contactMessage.setEmail(request.getEmail().trim().toLowerCase());
        contactMessage.setPhone(trimToNull(request.getPhone()));
        contactMessage.setSubject(request.getSubject().trim());
        contactMessage.setMessage(request.getMessage().trim());
        contactMessage.setStatus("NEW");
        contactMessage.setCreatedAt(LocalDateTime.now());

        ContactMessage savedMessage = contactMessageRepository.save(contactMessage);
        savedMessage.setNotificationSent(sendNotification(savedMessage));
        return contactMessageRepository.save(savedMessage);
    }

    private boolean sendNotification(ContactMessage contactMessage) {
        if (notificationEmail == null || notificationEmail.isBlank()) {
            return false;
        }

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(notificationEmail);
        email.setReplyTo(contactMessage.getEmail());
        email.setSubject("[EduFlow Contact #" + contactMessage.getContactMessageId() + "] "
                + contactMessage.getSubject());
        email.setText(
                "Họ tên: " + contactMessage.getFullName() + "\n"
                        + "Email: " + contactMessage.getEmail() + "\n"
                        + "Điện thoại: " + (contactMessage.getPhone() == null ? "Không cung cấp" : contactMessage.getPhone()) + "\n\n"
                        + contactMessage.getMessage()
        );

        try {
            mailSender.send(email);
            return true;
        } catch (MailException exception) {
            return false;
        }
    }

    private String trimToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
