package com.riskanalyzer.dto.response;

import com.riskanalyzer.enums.RiskLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExitSimulationResponse {
    private Long employeeId;
    private String employeeName;
    private String department;

    private int totalModulesOwned;
    private List<String> affectedModules;
    private List<String> orphanedModules;

    private List<String> skillsThatBecomeMissing;
    private List<String> skillsThatBecomeRare;

    private RiskLevel riskBefore;
    private RiskLevel riskAfter;
    private String overallImpact;
    private List<String> recommendations;
}