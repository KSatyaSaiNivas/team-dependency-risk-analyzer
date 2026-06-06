package com.riskanalyzer.dto.response;

import com.riskanalyzer.enums.SkillCategory;
import com.riskanalyzer.enums.SkillCriticality;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillResponse {
    private Long id;
    private String name;
    private SkillCategory category;
    private SkillCriticality criticality;
}