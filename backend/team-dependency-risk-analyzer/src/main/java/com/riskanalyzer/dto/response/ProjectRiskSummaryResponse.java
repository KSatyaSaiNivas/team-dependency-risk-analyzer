package com.riskanalyzer.dto.response;

import com.riskanalyzer.enums.RiskLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProjectRiskSummaryResponse {
    private Long projectId;
    private String projectName;
    private int totalModules;
    private long highRiskModules;
    private long criticalRiskModules;
    private long mediumRiskModules;
    private long lowRiskModules;
    private RiskLevel overallRiskLevel;
    private List<ModuleRiskResponse> moduleRisks;
}