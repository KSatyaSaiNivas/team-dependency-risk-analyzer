package com.riskanalyzer.service.impl;

import com.riskanalyzer.dto.request.CreateModuleRequest;
import com.riskanalyzer.dto.response.ModuleResponse;
import com.riskanalyzer.entity.Module;
import com.riskanalyzer.entity.Project;
import com.riskanalyzer.exception.ResourceNotFoundException;
import com.riskanalyzer.repository.ModuleRepository;
import com.riskanalyzer.repository.ProjectRepository;
import com.riskanalyzer.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final ProjectRepository projectRepository;

    @Override
    public ModuleResponse createModule(CreateModuleRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + request.getProjectId()));
        Module module = Module.builder()
                .project(project)
                .name(request.getName())
                .description(request.getDescription())
                .criticality(request.getCriticality())
                .build();
        return mapToResponse(moduleRepository.save(module));
    }

    @Override
    public ModuleResponse getModuleById(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + id));
        return mapToResponse(module);
    }

    @Override
    public List<ModuleResponse> getModulesByProject(Long projectId) {
        return moduleRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ModuleResponse updateModule(Long id, CreateModuleRequest request) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + id));
        module.setName(request.getName());
        module.setDescription(request.getDescription());
        module.setCriticality(request.getCriticality());
        return mapToResponse(moduleRepository.save(module));
    }

    @Override
    public void deleteModule(Long id) {
        if (!moduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Module not found with id: " + id);
        }
        moduleRepository.deleteById(id);
    }

    private ModuleResponse mapToResponse(Module module) {
        return ModuleResponse.builder()
                .id(module.getId())
                .projectId(module.getProject().getId())
                .projectName(module.getProject().getName())
                .name(module.getName())
                .description(module.getDescription())
                .criticality(module.getCriticality())
                .createdAt(module.getCreatedAt())
                .build();
    }
}