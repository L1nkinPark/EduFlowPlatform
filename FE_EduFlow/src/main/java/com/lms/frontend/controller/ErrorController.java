package com.lms.frontend.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    @RequestMapping("/error")
    public String showErrorPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        int statusCode = resolveStatus(request);
        response.setStatus(statusCode);
        if (statusCode == HttpServletResponse.SC_NOT_FOUND) {
            return "404";
        }

        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", "Hệ thống đang tạm thời gián đoạn. Vui lòng thử lại sau.");
        return "error";
    }

    private int resolveStatus(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status instanceof Integer statusCode && statusCode >= 400 && statusCode <= 599) {
            return statusCode;
        }
        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }
}
