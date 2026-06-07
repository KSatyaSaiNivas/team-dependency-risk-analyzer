package com.riskanalyzer.service;

import com.riskanalyzer.dto.response.ModuleRiskResponse;
import com.riskanalyzer.dto.response.ProjectRiskSummaryResponse;

import java.util.List;

public interface RiskAnalysisService {

    ModuleRiskResponse calculateModuleRisk(Long moduleId);

    ProjectRiskSummaryResponse calculateProjectRisk(Long projectId);

    List<ModuleRiskResponse> getAllHighRiskModules();

    List<ModuleRiskResponse> calculateAndSaveAllRisks();
}