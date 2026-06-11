package com.lms.frontend.config;

import com.lms.frontend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class AuthConfig implements WebMvcConfigurer {
    @Autowired
    private JwtAuthenticationFilter securityInterceptor;

    @Bean
    public RestTemplate customRestTemplate() {
        return new RestTemplate();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Áp dụng cho các URL trong danh sách
        registry.addInterceptor(securityInterceptor)
                .addPathPatterns("/student/**", "/instructor/**", "/admin/**");
    }


}
