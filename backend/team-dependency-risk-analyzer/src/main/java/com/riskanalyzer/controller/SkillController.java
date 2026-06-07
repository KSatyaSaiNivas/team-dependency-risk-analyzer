package com.riskanalyzer.controller;

import com.riskanalyzer.dto.request.CreateSkillRequest;
import com.riskanalyzer.dto.response.ApiResponse;
import com.riskanalyzer.dto.response.SkillResponse;
import com.riskanalyzer.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SkillController {

    private final SkillService skillService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SkillResponse>> createSkill(
            @Valid @RequestBody CreateSkillRequest request) {

        SkillResponse response = skillService.createSkill(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Skill created successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SkillResponse>>> getAllSkills() {

        List<SkillResponse> skills = skillService.getAllSkills();
        return ResponseEntity.ok(
                ApiResponse.success("Skills fetched successfully", skills));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SkillResponse>> getSkillById(
            @PathVariable Long id) {

        SkillResponse response = skillService.getSkillById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Skill fetched successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SkillResponse>> updateSkill(
            @PathVariable Long id,
            @Valid @RequestBody CreateSkillRequest request) {

        SkillResponse response = skillService.updateSkill(id, request);
        return ResponseEntity.ok(
                ApiResponse.success("Skill updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSkill(
            @PathVariable Long id) {

        skillService.deleteSkill(id);
        return ResponseEntity.ok(
                ApiResponse.success("Skill deleted successfully", null));
    }
}