package com.company.managementsystem.dto.response;

import com.company.managementsystem.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String email;
    private Role role;
    private Long employeeId;  // user has an associated employee
}