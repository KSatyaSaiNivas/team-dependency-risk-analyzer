package com.riskanalyzer.service;

import com.riskanalyzer.dto.request.AssignSkillRequest;
import com.riskanalyzer.dto.response.SkillResponse;

import java.util.List;

public interface EmployeeSkillService {
    void assignSkillToEmployee(AssignSkillRequest request);
    List<SkillResponse> getSkillsByEmployee(Long employeeId);
    void removeSkillFromEmployee(Long employeeId, Long skillId);
}