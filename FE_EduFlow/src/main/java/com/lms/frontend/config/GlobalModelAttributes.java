package com.lms.frontend.config;

import com.lms.frontend.util.ConstantUtil;
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
}
