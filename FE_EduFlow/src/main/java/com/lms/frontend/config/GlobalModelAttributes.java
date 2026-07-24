package com.lms.frontend.config;

import com.lms.frontend.util.ConstantUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Injects the backend URL into all Thymeleaf templates as a model attribute.
 * This allows templates to use [[${backendUrl}]] instead of hardcoding localhost.
 */
@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("backendUrl")
    public String backendUrl() {
        return ConstantUtil.HOST_URL;
    }

    // Thymeleaf 3.1+ no longer allows direct access to the '#request' utility
    // object in expressions, so the current request URI is exposed here as a
    // regular model attribute for use in nav "active" state checks.
    @ModelAttribute("currentUri")
    public String currentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
