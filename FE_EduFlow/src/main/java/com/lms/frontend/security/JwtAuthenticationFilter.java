package com.lms.frontend.security;

import com.lms.frontend.model.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Locale;

@Component
public class JwtAuthenticationFilter implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

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

        try {
            if (!jwtUtil.isTokenValid(userLogin.getAccessToken(), userLogin.getUsername())) {
                session.invalidate();
                return redirectToSignIn(request, response);
            }
        } catch (RuntimeException invalidToken) {
            // A malformed/expired token is an authentication failure, not a server error.
            session.invalidate();
            return redirectToSignIn(request, response);
        }

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
