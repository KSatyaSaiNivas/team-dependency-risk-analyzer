package com.riskanalyzer.controller;

import com.riskanalyzer.dto.response.ApiResponse;
import com.riskanalyzer.dto.response.ProjectSkillGapResponse;
import com.riskanalyzer.dto.response.SkillGapResponse;
import com.riskanalyzer.service.SkillGapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skill-gap")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SkillGapController {

    private final SkillGapService skillGapService;

    @GetMapping("/module/{moduleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SkillGapResponse>>> getModuleGaps(
            @PathVariable Long moduleId) {

        List<SkillGapResponse> gaps =
                skillGapService.analyzeModuleSkillGaps(moduleId);
        return ResponseEntity.ok(
                ApiResponse.success("Module skill gaps analyzed", gaps));
    }

    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ProjectSkillGapResponse>> getProjectGaps(
            @PathVariable Long projectId) {

        ProjectSkillGapResponse response =
                skillGapService.analyzeProjectSkillGaps(projectId);
        return ResponseEntity.ok(
                ApiResponse.success("Project skill gaps analyzed", response));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<SkillGapResponse>>> getAllGaps() {

        List<SkillGapResponse> gaps = skillGapService.getAllSkillGaps();
        return ResponseEntity.ok(
                ApiResponse.success("All skill gaps fetched", gaps));
    }
}