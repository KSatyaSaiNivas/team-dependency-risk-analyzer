package com.riskanalyzer.dto.request;

import com.riskanalyzer.enums.CriticalityLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateModuleRequest {

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotBlank(message = "Module name is required")
    private String name;

    private String description;
    private CriticalityLevel criticality = CriticalityLevel.MEDIUM;
}