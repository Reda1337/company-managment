package com.company.managementsystem.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EmployeeResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String position;
    private Double salary;
    private DepartmentSummary department;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    public static class DepartmentSummary {
        private Long id;
        private String name;
    }
}