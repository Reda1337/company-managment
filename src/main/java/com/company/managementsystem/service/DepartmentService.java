package com.company.managementsystem.service;

import com.company.managementsystem.entity.Department;
import com.company.managementsystem.dto.response.DepartmentResponse;
import com.company.managementsystem.dto.request.DepartmentCreateRequest;
import com.company.managementsystem.repository.DepartmentRepository;
import com.company.managementsystem.exception.BusinessException;
import com.company.managementsystem.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public DepartmentResponse createDepartment(DepartmentCreateRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new BusinessException("Department already exists with name: " + request.getName());
        }

        Department department = modelMapper.map(request, Department.class);
        Department savedDepartment = departmentRepository.save(department);

        DepartmentResponse response = modelMapper.map(savedDepartment, DepartmentResponse.class);
        response.setEmployeeCount(0);
        return response;
    }

    public DepartmentResponse getDepartmentById (Long id) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Department not found with id: " + id
            ));
        DepartmentResponse response = modelMapper.map(department, DepartmentResponse.class);
        response.setEmployeeCount(department.getEmployees().size());
        return response;
    }

    public Page<DepartmentResponse> getAllDepartments (Pageable pageable) {
        return departmentRepository.findAll(pageable)
            .map(department -> {
               DepartmentResponse response = modelMapper.map(department, DepartmentResponse.class);
               response.setEmployeeCount(department.getEmployees().size());
               return response;
            });
    }

    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Department not found with id: " + id
            ));

        if (!department.getEmployees().isEmpty()) {
            throw new BusinessException(
                    "Cannot delete department with employees. Please reassign employees first."
            );
        }

        departmentRepository.deleteById(id);
    }
}