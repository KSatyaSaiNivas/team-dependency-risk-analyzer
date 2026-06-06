package com.riskanalyzer.dto.response;

import com.riskanalyzer.enums.RiskLevel;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class RiskScoreResponse {
    private Long id;
    private Long moduleId;
    private String moduleName;
    private String projectName;
    private RiskLevel riskLevel;
    private BigDecimal riskScore;
    private String riskReason;
    private LocalDateTime calculatedAt;
}