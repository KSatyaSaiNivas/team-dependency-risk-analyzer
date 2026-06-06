package com.riskanalyzer.service;

import com.riskanalyzer.dto.request.CreateEmployeeRequest;
import com.riskanalyzer.dto.response.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse createEmployee(CreateEmployeeRequest request);
    EmployeeResponse getEmployeeById(Long id);
    List<EmployeeResponse> getAllEmployees();
    List<EmployeeResponse> getActiveEmployees();
    EmployeeResponse updateEmployee(Long id, CreateEmployeeRequest request);
    void deactivateEmployee(Long id);
}