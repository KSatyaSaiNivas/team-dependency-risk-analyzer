
package com.riskanalyzer.dto.response;

import com.riskanalyzer.enums.Proficiency;
import lombok.Builder;
import lombok.Data;

import java.util.List;

    @Data
    @Builder
    public class SkillGapResponse {
        private Long moduleId;
        private String moduleName;
        private String projectName;
        private String skillName;
        private Proficiency requiredLevel;
        private int availableEmployeeCount;
        private List<String> availableEmployees;
        private boolean hasGap;
        private String gapReason;

}
