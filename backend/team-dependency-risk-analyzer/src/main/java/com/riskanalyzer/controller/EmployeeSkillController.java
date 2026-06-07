package com.riskanalyzer.controller;

import com.riskanalyzer.dto.request.AssignSkillRequest;
import com.riskanalyzer.dto.response.ApiResponse;
import com.riskanalyzer.dto.response.SkillResponse;
import com.riskanalyzer.service.EmployeeSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-skills")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmployeeSkillController {

    private final EmployeeSkillService employeeSkillService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> assignSkill(
            @Valid @RequestBody AssignSkillRequest request) {

        employeeSkillService.assignSkillToEmployee(request);
        return ResponseEntity.ok(
                ApiResponse.success("Skill assigned successfully", null));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SkillResponse>>> getSkillsByEmployee(
            @PathVariable Long employeeId) {

        List<SkillResponse> skills =
                employeeSkillService.getSkillsByEmployee(employeeId);
        return ResponseEntity.ok(
                ApiResponse.success("Skills fetched successfully", skills));
    }

    @DeleteMapping("/employee/{employeeId}/skill/{skillId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> removeSkill(
            @PathVariable Long employeeId,
            @PathVariable Long skillId) {

        employeeSkillService.removeSkillFromEmployee(employeeId, skillId);
        return ResponseEntity.ok(
                ApiResponse.success("Skill removed successfully", null));
    }
}