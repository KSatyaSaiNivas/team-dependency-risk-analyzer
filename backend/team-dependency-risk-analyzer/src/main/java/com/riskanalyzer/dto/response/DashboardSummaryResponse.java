package com.riskanalyzer.dto.response;

import com.riskanalyzer.enums.RiskLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardSummaryResponse {
    private int totalProjects;
    private int totalModules;
    private int totalEmployees;
    private int totalSkills;

    private long criticalRiskModules;
    private long highRiskModules;
    private long mediumRiskModules;
    private long lowRiskModules;

    private long orphanedModules;
    private long singleOwnerModules;

    private RiskLevel overallSystemRisk;
    private List<ModuleRiskResponse> top5HighRiskModules;
    private List<ProjectRiskSummaryResponse> projectRiskSummaries;
}
