package com.riskanalyzer.dto.response;

import com.riskanalyzer.enums.CriticalityLevel;
import com.riskanalyzer.enums.RiskLevel;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ModuleRiskResponse {
    private Long moduleId;
    private String moduleName;
    private CriticalityLevel moduleCriticality;
    private Long projectId;
    private String projectName;
    private int ownerCount;
    private List<String> ownerNames;
    private List<String> rareSkills;
    private BigDecimal riskScore;
    private RiskLevel riskLevel;
    private String riskReason;
}
