package com.riskanalyzer.controller;

import com.riskanalyzer.dto.request.CreateProjectRequest;
import com.riskanalyzer.dto.response.ApiResponse;
import com.riskanalyzer.dto.response.ProjectResponse;
import com.riskanalyzer.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @Valid @RequestBody CreateProjectRequest request) {

        ProjectResponse response = projectService.createProject(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Project created successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects() {

        List<ProjectResponse> projects = projectService.getAllProjects();
        return ResponseEntity.ok(
                ApiResponse.success("Projects fetched successfully", projects));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(
            @PathVariable Long id) {

        ProjectResponse response = projectService.getProjectById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Project fetched successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody CreateProjectRequest request) {

        ProjectResponse response = projectService.updateProject(id, request);
        return ResponseEntity.ok(
                ApiResponse.success("Project updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProject(
            @PathVariable Long id) {

        projectService.deleteProject(id);
        return ResponseEntity.ok(
                ApiResponse.success("Project deleted successfully", null));
    }
}
