package com.riskanalyzer.controller;

import com.riskanalyzer.dto.response.ApiResponse;
import com.riskanalyzer.dto.response.ModuleRiskResponse;
import com.riskanalyzer.dto.response.ProjectRiskSummaryResponse;
import com.riskanalyzer.service.RiskAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RiskAnalysisController {

    private final RiskAnalysisService riskAnalysisService;

    @GetMapping("/module/{moduleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ModuleRiskResponse>> getModuleRisk(
            @PathVariable Long moduleId) {

        ModuleRiskResponse response =
                riskAnalysisService.calculateModuleRisk(moduleId);
        return ResponseEntity.ok(
                ApiResponse.success("Module risk calculated", response));
    }

    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ProjectRiskSummaryResponse>> getProjectRisk(
            @PathVariable Long projectId) {

        ProjectRiskSummaryResponse response =
                riskAnalysisService.calculateProjectRisk(projectId);
        return ResponseEntity.ok(
                ApiResponse.success("Project risk calculated", response));
    }

    @GetMapping("/high-risk-modules")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<ModuleRiskResponse>>> getHighRiskModules() {

        List<ModuleRiskResponse> response =
                riskAnalysisService.getAllHighRiskModules();
        return ResponseEntity.ok(
                ApiResponse.success("High risk modules fetched", response));
    }

    @PostMapping("/calculate-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ModuleRiskResponse>>> calculateAllRisks() {

        List<ModuleRiskResponse> response =
                riskAnalysisService.calculateAndSaveAllRisks();
        return ResponseEntity.ok(
                ApiResponse.success("All risks calculated and saved", response));
    }
}