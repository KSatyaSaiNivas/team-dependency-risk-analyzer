package com.riskanalyzer.service;

import com.riskanalyzer.dto.response.ProjectSkillGapResponse;
import com.riskanalyzer.dto.response.SkillGapResponse;

import java.util.List;

public interface SkillGapService {
    List<SkillGapResponse> analyzeModuleSkillGaps(Long moduleId);
    ProjectSkillGapResponse analyzeProjectSkillGaps(Long projectId);
    List<SkillGapResponse> getAllSkillGaps();
}