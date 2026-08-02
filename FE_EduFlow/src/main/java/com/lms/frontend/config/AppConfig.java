package com.lms.frontend.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.time.Duration;

@Configuration
public class AppConfig implements WebMvcConfigurer {
//
//
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder,
                                     @Value("${backend.connect-timeout:3s}") Duration connectTimeout,
                                     @Value("${backend.read-timeout:10s}") Duration readTimeout) {
        return builder
                .setConnectTimeout(connectTimeout)
                .setReadTimeout(readTimeout)
                .build();
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
        org.springframework.web.servlet.i18n.CookieLocaleResolver resolver =
                new org.springframework.web.servlet.i18n.CookieLocaleResolver("EDUFLOW_LOCALE");
        resolver.setDefaultLocale(java.util.Locale.forLanguageTag("vi"));
        resolver.setCookieMaxAge(java.time.Duration.ofDays(365));
        return resolver;
    }

    @Bean
    public org.springframework.web.servlet.i18n.LocaleChangeInterceptor localeChangeInterceptor() {
        org.springframework.web.servlet.i18n.LocaleChangeInterceptor lci = new org.springframework.web.servlet.i18n.LocaleChangeInterceptor();
        lci.setParamName("lang");
        lci.setIgnoreInvalidLocale(true);
        return lci;
    }

    @Override
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
