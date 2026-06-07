package com.riskanalyzer.service.impl;

import com.riskanalyzer.dto.request.AssignOwnerRequest;
import com.riskanalyzer.dto.response.EmployeeResponse;
import com.riskanalyzer.entity.Employee;
import com.riskanalyzer.entity.Module;
import com.riskanalyzer.entity.ModuleOwner;
import com.riskanalyzer.exception.ResourceNotFoundException;
import com.riskanalyzer.repository.EmployeeRepository;
import com.riskanalyzer.repository.ModuleOwnerRepository;
import com.riskanalyzer.repository.ModuleRepository;
import com.riskanalyzer.service.ModuleOwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleOwnerServiceImpl implements ModuleOwnerService {

    private final ModuleOwnerRepository moduleOwnerRepository;
    private final ModuleRepository moduleRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public void assignOwner(AssignOwnerRequest request) {
        Module module = moduleRepository.findById(request.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Module not found: " + request.getModuleId()));

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found: " + request.getEmployeeId()));

        ModuleOwner owner = ModuleOwner.builder()
                .module(module)
                .employee(employee)
                .ownershipType(request.getOwnershipType())
                .assignedDate(LocalDate.now())
                .isActive(true)
                .build();

        moduleOwnerRepository.save(owner);
    }

    @Override
    public List<EmployeeResponse> getOwnersByModule(Long moduleId) {
        return moduleOwnerRepository
                .findByModuleIdAndIsActiveTrue(moduleId)
                .stream()
                .map(mo -> EmployeeResponse.builder()
                        .id(mo.getEmployee().getId())
                        .fullName(mo.getEmployee().getFullName())
                        .email(mo.getEmployee().getEmail())
                        .department(mo.getEmployee().getDepartment())
                        .designation(mo.getEmployee().getDesignation())
                        .status(mo.getEmployee().getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void removeOwner(Long moduleOwnerId) {
        ModuleOwner owner = moduleOwnerRepository.findById(moduleOwnerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Module owner not found: " + moduleOwnerId));
        owner.setIsActive(false);
        owner.setRelievedDate(LocalDate.now());
        moduleOwnerRepository.save(owner);
    }
}