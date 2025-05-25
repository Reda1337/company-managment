package com.company.managementsystem.controller;

import com.company.managementsystem.dto.request.DepartmentCreateRequest;
import com.company.managementsystem.dto.response.ApiResponse;
import com.company.managementsystem.dto.response.DepartmentResponse;
import com.company.managementsystem.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(
            @Valid @RequestBody DepartmentCreateRequest request) {
        DepartmentResponse department = departmentService.createDepartment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Department created successfully", department));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartment (@PathVariable Long id) {
        DepartmentResponse department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success("Department retrieved successfully", department));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DepartmentResponse>>> getAllDepartments (
        @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<DepartmentResponse> departments = departmentService.getAllDepartments(pageable);
        return ResponseEntity.ok(ApiResponse.success("Departments retrieved successfully", departments));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment (@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department deleted successfully", null));
    }
}
