package com.riskanalyzer.dto.request;

import com.riskanalyzer.enums.Proficiency;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignSkillRequest {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Skill ID is required")
    private Long skillId;

    @NotNull(message = "Proficiency is required")
    private Proficiency proficiency;
}