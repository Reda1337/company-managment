package com.company.managementsystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeUpdateRequest {

    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Email(message = "Email must be valid")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be valid")
    private String phone;

    private String position;

    @DecimalMin(value = "0.0", message = "Salary must be positive")
    private Double salary;

    private Long departmentId;
}