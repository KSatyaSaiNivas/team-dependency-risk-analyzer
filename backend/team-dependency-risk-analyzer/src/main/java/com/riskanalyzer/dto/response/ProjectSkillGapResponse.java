package com.riskanalyzer.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

    @Data
    @Builder
    public class ProjectSkillGapResponse {
        private Long projectId;
        private String projectName;
        private int totalSkillsRequired;
        private int totalGapsFound;
        private List<SkillGapResponse> skillGaps;

}
