package com.company.managementsystem.service;

import com.company.managementsystem.dto.request.EmployeeCreateRequest;
import com.company.managementsystem.dto.request.EmployeeUpdateRequest;
import com.company.managementsystem.dto.response.EmployeeResponse;
import com.company.managementsystem.entity.Department;
import com.company.managementsystem.entity.Employee;
import com.company.managementsystem.exception.BusinessException;
import com.company.managementsystem.exception.ResourceNotFoundException;
import com.company.managementsystem.repository.DepartmentRepository;
import com.company.managementsystem.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {

        // Create employee entity
        Employee employee = new Employee();
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setPhone(request.getPhone());
        employee.setPosition(request.getPosition());
        employee.setSalary(request.getSalary());

        // Assign department if provided
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Department not found with id: " + request.getDepartmentId()
                ));
            employee.setDepartment(department);
        }

        // save and return
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToResponse(savedEmployee);
    }

    private EmployeeResponse convertToResponse(Employee employee) {
        EmployeeResponse response = modelMapper.map(employee, EmployeeResponse.class);

        // Set email from User if exists
        if (employee.getUser() != null) {
            response.setEmail(employee.getUser().getEmail());
        }

        // Handle the department mapping explicitly
        if (employee.getDepartment() != null) {
            EmployeeResponse.DepartmentSummary deptSummary = new EmployeeResponse.DepartmentSummary();
            deptSummary.setId(employee.getDepartment().getId());
            deptSummary.setName(employee.getDepartment().getName());
            response.setDepartment(deptSummary);
        }

        return response;
    }

    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Employee not found with id: " + id
            ));
        return modelMapper.map(employee, EmployeeResponse.class);
    }

    public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable)
            .map(employee -> modelMapper.map(employee, EmployeeResponse.class));
    }

    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Employee not found with id: " + id
            ));

        // update only provided fields
        if (request.getFirstName() != null) {
            employee.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            employee.setLastName(request.getLastName());
        }
        if (request.getPhone() != null) {
            employee.setPhone(request.getPhone());
        }
        if (request.getPosition() != null) {
            employee.setPosition(request.getPosition());
        }
        if (request.getSalary() != null) {
            employee.setSalary(request.getSalary());
        }

        // handle department change
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Department not found with id: " + request.getDepartmentId()
                ));
            employee.setDepartment(department);
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        return modelMapper.map(updatedEmployee, EmployeeResponse.class);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    public List<EmployeeResponse> getEmployeesByDepartment (Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId).stream()
            .map(employee -> modelMapper.map(employee, EmployeeResponse.class))
            .collect(Collectors.toList());
    }
}