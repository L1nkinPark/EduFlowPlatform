package com.lms.backend.security;

import com.lms.backend.exception.InvalidTokenException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // /api/auth/login và /api/auth/refresh-token không cần (và không nên) đọc JWT hiện có.
            // Riêng /api/auth/register vẫn cần xử lý JWT nếu có, để biết người gọi có phải ADMIN
            // hay không (phục vụ việc chỉ cho ADMIN tạo account INSTRUCTOR/ADMIN).
            if (request.getServletPath().contains("/api/auth/login")
                    || request.getServletPath().contains("/api/auth/refresh-token")) {
                filterChain.doFilter(request, response);
                return;
            }
            if (request.getServletPath().contains("/v3/api-docs") || request.getServletPath().contains("/swagger-ui")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Lấy JWT từ request
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String username;

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Lấy username từ chuỗi JWT
            jwt = authHeader.substring(7);
            try {
                username = jwtToken.extractUsername(jwt);
            } catch (JwtException ex) {
                // Token hết hạn hoặc không hợp lệ: bỏ qua và xử lý request như ẩn danh
                // để không làm gãy các endpoint public (ví dụ /api/courses).
                filterChain.doFilter(request, response);
                return;
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Lấy thông tin người dùng
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Nếu người dùng hợp lệ, set thông tin cho Seturity Context
                if (jwtToken.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    throw new InvalidTokenException("Invalid Token");
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            throw ex;
        }
    }

}
