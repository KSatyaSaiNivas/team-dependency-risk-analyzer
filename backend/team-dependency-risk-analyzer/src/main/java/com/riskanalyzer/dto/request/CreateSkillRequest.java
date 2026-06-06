package com.riskanalyzer.dto.request;

import com.riskanalyzer.enums.SkillCategory;
import com.riskanalyzer.enums.SkillCriticality;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateSkillRequest {

    @NotBlank(message = "Skill name is required")
    private String name;

    @NotNull(message = "Category is required")
    private SkillCategory category;

    private SkillCriticality criticality = SkillCriticality.COMMON;
}