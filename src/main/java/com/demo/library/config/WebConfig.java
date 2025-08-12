package com.demo.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // 改用更灵活的通配模式
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 增加 PATCH 和 OPTIONS
                .allowedHeaders("*") // 允许所有自定义头部
                .exposedHeaders("*") // 暴露所有头部
                .allowCredentials(true)
                .maxAge(3600);
    }
}