package com.riskanalyzer.dto.response;

import com.riskanalyzer.enums.CriticalityLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ModuleResponse {
    private Long id;
    private Long projectId;
    private String projectName;
    private String name;
    private String description;
    private CriticalityLevel criticality;
    private LocalDateTime createdAt;
}