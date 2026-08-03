package com.lms.frontend.security;

import com.lms.frontend.model.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Locale;

@Component
public class JwtAuthenticationFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return redirectToSignIn(request, response);
        }

        AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
        if (userLogin == null || userLogin.getAccessToken() == null || userLogin.getUsername() == null) {
            session.invalidate();
            return redirectToSignIn(request, response);
        }

        // This session is server-side and is populated only after backend login.
        // The backend remains the authority that validates the bearer token on
        // every protected API call; the frontend must not require a copy of the
        // backend signing secret merely to route an existing session.
        String requiredRole = requiredRole(request.getRequestURI(), request.getContextPath());
        String userRole = normalizeRole(userLogin.getRole());
        if (requiredRole != null && !requiredRole.equals(userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }

    private boolean redirectToSignIn(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect(request.getContextPath() + "/signin");
        return false;
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return null;
        }
        String normalized = role.trim().toUpperCase(Locale.ROOT);
        return normalized.startsWith("ROLE_") ? normalized.substring(5) : normalized;
    }

    private String requiredRole(String requestUri, String contextPath) {
        String path = requestUri;
        if (contextPath != null && !contextPath.isEmpty() && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }
        if (path.startsWith("/admin")) {
            return "ADMIN";
        }
        if (path.startsWith("/instructor")) {
            return "INSTRUCTOR";
        }
        if (path.startsWith("/student") || path.equals("/course/learn") || path.equals("/course/checkout")) {
            return "STUDENT";
        }
        return null;
    }

}
