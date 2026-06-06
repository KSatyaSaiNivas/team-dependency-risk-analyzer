package com.riskanalyzer.dto.response;

import com.riskanalyzer.enums.CriticalityLevel;
import com.riskanalyzer.enums.ProjectStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private ProjectStatus status;
    private CriticalityLevel criticality;
    private LocalDateTime createdAt;
}