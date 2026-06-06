package com.riskanalyzer.service;

import com.riskanalyzer.dto.request.CreateSkillRequest;
import com.riskanalyzer.dto.response.SkillResponse;

import java.util.List;

public interface SkillService {
    SkillResponse createSkill(CreateSkillRequest request);
    SkillResponse getSkillById(Long id);
    List<SkillResponse> getAllSkills();
    SkillResponse updateSkill(Long id, CreateSkillRequest request);
    void deleteSkill(Long id);
}