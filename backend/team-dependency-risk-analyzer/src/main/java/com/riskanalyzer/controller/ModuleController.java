package com.riskanalyzer.controller;

import com.riskanalyzer.dto.request.CreateModuleRequest;
import com.riskanalyzer.dto.response.ApiResponse;
import com.riskanalyzer.dto.response.ModuleResponse;
import com.riskanalyzer.service.ModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ModuleController {

    private final ModuleService moduleService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ModuleResponse>> createModule(
            @Valid @RequestBody CreateModuleRequest request) {

        ModuleResponse response = moduleService.createModule(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Module created successfully", response));
    }

    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<ModuleResponse>>> getModulesByProject(
            @PathVariable Long projectId) {

        List<ModuleResponse> modules = moduleService.getModulesByProject(projectId);
        return ResponseEntity.ok(
                ApiResponse.success("Modules fetched successfully", modules));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ModuleResponse>> getModuleById(
            @PathVariable Long id) {

        ModuleResponse response = moduleService.getModuleById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Module fetched successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ModuleResponse>> updateModule(
            @PathVariable Long id,
            @Valid @RequestBody CreateModuleRequest request) {

        ModuleResponse response = moduleService.updateModule(id, request);
        return ResponseEntity.ok(
                ApiResponse.success("Module updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteModule(
            @PathVariable Long id) {

        moduleService.deleteModule(id);
        return ResponseEntity.ok(
                ApiResponse.success("Module deleted successfully", null));
    }
}