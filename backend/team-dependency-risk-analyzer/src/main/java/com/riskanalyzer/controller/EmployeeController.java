package com.riskanalyzer.controller;

import com.riskanalyzer.dto.request.CreateEmployeeRequest;
import com.riskanalyzer.dto.response.ApiResponse;
import com.riskanalyzer.dto.response.EmployeeResponse;
import com.riskanalyzer.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request) {

        EmployeeResponse response = employeeService.createEmployee(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Employee created successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getAllEmployees() {

        List<EmployeeResponse> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(
                ApiResponse.success("Employees fetched successfully", employees));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployeeById(
            @PathVariable Long id) {

        EmployeeResponse response = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Employee fetched successfully", response));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getActiveEmployees() {

        List<EmployeeResponse> employees = employeeService.getActiveEmployees();
        return ResponseEntity.ok(
                ApiResponse.success("Active employees fetched", employees));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody CreateEmployeeRequest request) {

        EmployeeResponse response = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(
                ApiResponse.success("Employee updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deactivateEmployee(
            @PathVariable Long id) {

        employeeService.deactivateEmployee(id);
        return ResponseEntity.ok(
                ApiResponse.success("Employee deactivated successfully", null));
    }
}