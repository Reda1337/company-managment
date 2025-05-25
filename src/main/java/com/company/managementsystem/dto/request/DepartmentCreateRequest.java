package com.company.managementsystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DepartmentCreateRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
}