package com.lms.frontend.security;

import com.lms.frontend.model.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtAuthenticationFilter implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Lấy session từ request
        HttpSession session = httpRequest.getSession(false); // Không tạo session mới nếu chưa có
        if (session == null) {
            // Nếu chưa đăng nhập, chuyển hướng đến trang đăng nhập
            response.sendRedirect("/signin");
            return false;
        }

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        AuthResponse userLogin = (AuthResponse) session.getAttribute("userLogin");
        if (userLogin != null) {
            // Lấy token từ header
            String token = userLogin.getAccessToken();

            // Xác thực token
            if (token == null || !jwtUtil.isTokenValid(token, userLogin.getUsername())) {
                response.sendRedirect("/signin");
                return false;
            }

            // Kiểm tra quyền truy cập
            String requestURI = request.getRequestURI();
            String userRole = userLogin.getRole();

            if ((requestURI.startsWith("/admin") && !userRole.equals("ADMIN")) ||
                    (requestURI.startsWith("/instructor") && !userRole.equals("INSTRUCTOR"))) {
                // Nếu không có quyền, trả về trang lỗi (ví dụ: trang 401 Unauthorized)
                response.sendRedirect("/404");
                return false;
            }

            return true;
        }

        response.sendRedirect("/signin");
        return false;
    }

}
