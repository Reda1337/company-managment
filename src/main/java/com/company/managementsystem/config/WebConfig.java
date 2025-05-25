package com.company.managementsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements  WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Applies CORS configuration to ALL endpoints in your application
                .allowedOriginPatterns("*") // Specifies which origins are allowed to access your API : * means everyone
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true) // enables cross-origin requests with credentials
                .maxAge(3600); // How long browser can cache CORS preflight response
    }
}
