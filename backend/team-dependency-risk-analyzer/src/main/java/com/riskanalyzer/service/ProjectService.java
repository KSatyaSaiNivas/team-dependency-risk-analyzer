package com.riskanalyzer.service;

import com.riskanalyzer.dto.request.CreateProjectRequest;
import com.riskanalyzer.dto.response.ProjectResponse;

import java.util.List;

public interface ProjectService {
    ProjectResponse createProject(CreateProjectRequest request);
    ProjectResponse getProjectById(Long id);
    List<ProjectResponse> getAllProjects();
    ProjectResponse updateProject(Long id, CreateProjectRequest request);
    void deleteProject(Long id);
}