package com.riskanalyzer.dto.request;

import com.riskanalyzer.enums.CriticalityLevel;
import com.riskanalyzer.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateProjectRequest {

    @NotBlank(message = "Project name is required")
    private String name;

    private String description;
    private LocalDate startDate;
    private ProjectStatus status = ProjectStatus.ACTIVE;
    private CriticalityLevel criticality = CriticalityLevel.MEDIUM;
}