package com.lms.frontend.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class AppConfig implements WebMvcConfigurer {
//
//
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Value(value = "${cloudinary.cloud_name}") // @Value : Llấy thông tin từ tập tin .properties và gán vào biến
    private String cloudName;

    @Value(value = "${cloudinary.api_key}")
    private String apiKey;

    @Value(value = "${cloudinary.api_secret}")
    private String apiSecret;

    @Value(value = "${cloudinary.secure}")
    private String secure;

    @Bean
    public Cloudinary cloudinary() {
        Map config = new HashMap();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        config.put("secure", secure);
        return new Cloudinary(config);
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("voduchieu42@gmail.com");
        mailSender.setPassword("oesr rsnh nall cxyh"); // Mật khẩu ứng dụng Gmail

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        return mailSender;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:8888")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public org.springframework.web.servlet.LocaleResolver localeResolver() {
        org.springframework.web.servlet.i18n.SessionLocaleResolver slr = new org.springframework.web.servlet.i18n.SessionLocaleResolver();
        slr.setDefaultLocale(java.util.Locale.ENGLISH);
        return slr;
    }

    @Bean
    public org.springframework.web.servlet.i18n.LocaleChangeInterceptor localeChangeInterceptor() {
        org.springframework.web.servlet.i18n.LocaleChangeInterceptor lci = new org.springframework.web.servlet.i18n.LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
