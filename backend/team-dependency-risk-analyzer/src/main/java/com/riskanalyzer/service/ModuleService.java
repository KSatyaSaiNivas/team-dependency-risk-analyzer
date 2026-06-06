package com.riskanalyzer.service;

import com.riskanalyzer.dto.request.CreateModuleRequest;
import com.riskanalyzer.dto.response.ModuleResponse;

import java.util.List;

public interface ModuleService {
    ModuleResponse createModule(CreateModuleRequest request);
    ModuleResponse getModuleById(Long id);
    List<ModuleResponse> getModulesByProject(Long projectId);
    ModuleResponse updateModule(Long id, CreateModuleRequest request);
    void deleteModule(Long id);
}