package com.company.managementsystem.controller;

import com.company.managementsystem.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {

    private final Environment environment;
    private final Optional<BuildProperties> buildProperties;
    private final JdbcTemplate jdbcTemplate;

    @GetMapping
    public ApiResponse<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "Company Management System");
        health.put("profile", environment.getActiveProfiles());

        return ApiResponse.success(health);
    }

    @GetMapping("/detailed")
    public ApiResponse<Map<String, Object>> detailedHealth() {
        Map<String, Object> details = new HashMap<>();
        details.put("status", "UP");
        details.put("timestamp", LocalDateTime.now());

        // Application info
        Map<String, Object> app = new HashMap<>();
        app.put("name", environment.getProperty("spring.application.name"));
        app.put("port", environment.getProperty("server.port"));
        app.put("profile", environment.getActiveProfiles());
        buildProperties.ifPresent(props -> {
            app.put("version", props.getVersion());
            app.put("buildTime", props.getTime());
        });
        details.put("application", app);

        // System info
        Map<String, Object> system = new HashMap<>();
        system.put("java", System.getProperty("java.version"));
        system.put("os", System.getProperty("os.name"));
        system.put("processors", Runtime.getRuntime().availableProcessors());
        system.put("maxMemory", Runtime.getRuntime().maxMemory() / 1048576 + " MB");
        system.put("totalMemory", Runtime.getRuntime().totalMemory() / 1048576 + " MB");
        system.put("freeMemory", Runtime.getRuntime().freeMemory() / 1048576 + " MB");
        details.put("system", system);

        // Database info
        try {
            Map<String, Object> database = new HashMap<>();
            database.put("status", "UP");
            database.put("type", "PostgreSQL");
            String version = jdbcTemplate.queryForObject("SELECT version()", String.class);
            database.put("version", version);
            details.put("database", database);

        } catch (Exception e) {
            Map<String, Object> database = new HashMap<>();
            database.put("status", "DOWN");
            database.put("error", e.getMessage());
            details.put("database", database);
        }

        return ApiResponse.success(details);
    }
}
