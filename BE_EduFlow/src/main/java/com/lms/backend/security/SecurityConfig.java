package com.lms.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
//
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    private final String[] ENDPOINT_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/public",
            "/api/auth/**",
            "/css/**",
            "/js/**",
            "/images/**",
            "/api/**",
            "/actuator/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Bật CORS với cấu hình tùy chỉnh
                .csrf(csrf -> csrf.disable()) // Tắt CSRF nếu không cần
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll() // Cho phép truy cập không cần xác thực vào endpoint này
                        .requestMatchers(ENDPOINT_WHITELIST).permitAll() // Cho phép tất cả mọi người truy cập vào những URL này
                        .anyRequest().authenticated()) // Tất cả các request còn lại cần phải xác thực mới được truy cập
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Thêm lớp Filter kiểm tra JWT

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Read allowed origins from environment variable, fallback to localhost
        String corsOrigins = System.getenv("CORS_ALLOWED_ORIGINS");
        if (corsOrigins != null && !corsOrigins.isEmpty()) {
            configuration.setAllowedOrigins(List.of(corsOrigins.split(",")));
        } else {
            configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:8080",
                "http://localhost:8888"
            ));
        }

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // Cho phép gửi cookies hoặc thông tin xác thực nếu cần

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
