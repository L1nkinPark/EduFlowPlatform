package com.lms.frontend.controller;

import com.lms.frontend.model.request.ContactMessageRequest;
import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.AuthResponse;
import com.lms.frontend.service.ContactMessageService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class ContactController {

    @Autowired
    private ContactMessageService contactMessageService;

    @GetMapping("/contact")
    public String showContactPage(Model model, HttpSession session) {
        if (!model.containsAttribute("contactRequest")) {
            ContactMessageRequest request = new ContactMessageRequest();
            AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
            if (userLogin != null) {
                request.setFullName(userLogin.getFullName());
            }
            model.addAttribute("contactRequest", request);
        }
        return "contact";
    }

    @PostMapping("/contact")
    public String submitContact(@Valid @ModelAttribute("contactRequest") ContactMessageRequest request,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (request.getWebsite() != null && !request.getWebsite().isBlank()) {
            redirectAttributes.addFlashAttribute("contactSuccess", true);
            return "redirect:/contact";
        }
        if (bindingResult.hasErrors()) {
            return "contact";
        }

        ApiResponse<Map<String, Object>> response = contactMessageService.submit(request);
        if (response == null || !"SUCCESS".equals(response.getStatus()) || response.getPayload() == null) {
            bindingResult.reject("contact.submit.failed");
            return "contact";
        }

        redirectAttributes.addFlashAttribute("contactSuccess", true);
        redirectAttributes.addFlashAttribute("contactReference", response.getPayload().get("referenceId"));
        return "redirect:/contact";
    }
}
