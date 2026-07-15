package com.ibm.employee.controller;

import java.util.List;

import com.ibm.employee.dto.request.CreateEmployeeRequest;
import com.ibm.employee.dto.request.UpdateEmployeeRequest;
import com.ibm.employee.dto.request.UpdateStatusRequest;

import com.ibm.employee.dto.response.EmployeeResponse;
import com.ibm.employee.entity.enums.EmploymentStatus;
import com.ibm.employee.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

//    Create Employee
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse>
    createEmployee(
            @Valid
            @RequestBody CreateEmployeeRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService
                        .createEmployee(request));
    }

//    Gte Employee By Id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<EmployeeResponse>
    getEmployeeById(@PathVariable String id) {

        return ResponseEntity.ok(
                employeeService.getEmployeeById(id));
    }
    
//    Update Employee
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    public ResponseEntity<EmployeeResponse>
    updateEmployee(
            @PathVariable String id,
            @RequestBody UpdateEmployeeRequest request) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(
                        id,
                        request));
    }
    
//    Soft Delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable String id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }
    
//    Get by Code
    @GetMapping("/code/{employeeCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<EmployeeResponse>
    getByCode(
            @PathVariable String employeeCode) {

        return ResponseEntity.ok(
                employeeService
                        .getEmployeeByCode(
                                employeeCode));
    }
    
//    Get by Department
    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<EmployeeResponse>>
    getByDepartment(
            @PathVariable String departmentId) {

        return ResponseEntity.ok(
                employeeService.getByDepartment(
                        departmentId));
    }
    
//    Get by Designation
    @GetMapping("/designation/{designationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<EmployeeResponse>>
    getByDesignation(
            @PathVariable String designationId) {

        return ResponseEntity.ok(
                employeeService.getByDesignation(
                        designationId));
    }
    
//    Get by Manager
    @GetMapping("/manager/{managerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'MANAGER')")
    public ResponseEntity<List<EmployeeResponse>>
    getByManager(
            @PathVariable String managerId) {

        return ResponseEntity.ok(
                employeeService.getByManager(
                        managerId));
    }
    
//    Get by Status
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<List<EmployeeResponse>>
    getByStatus(
            @PathVariable EmploymentStatus status) {

        return ResponseEntity.ok(
                employeeService.getByStatus(status));
    }
    
//    Update Status
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<EmployeeResponse>
    updateStatus(
            @PathVariable String id,
            @RequestBody UpdateStatusRequest request) {

        return ResponseEntity.ok(
                employeeService.updateStatus(
                        id,
                        request));
    }
    
//    Pagination API
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<Page<EmployeeResponse>>
    getAllEmployees(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "firstName")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction) {

        return ResponseEntity.ok(
                employeeService.getAllEmployees(
                        page,
                        size,
                        sortBy,
                        direction));
    }
    
//    Get All Employees
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {

        return ResponseEntity.ok(
                employeeService.getAllEmployees()
        );
    }
}