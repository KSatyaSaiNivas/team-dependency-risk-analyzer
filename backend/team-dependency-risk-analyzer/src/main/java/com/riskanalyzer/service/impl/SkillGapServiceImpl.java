package com.riskanalyzer.service.impl;

import com.riskanalyzer.dto.response.ProjectSkillGapResponse;
import com.riskanalyzer.dto.response.SkillGapResponse;
import com.riskanalyzer.entity.Module;
import com.riskanalyzer.entity.ModuleOwner;
import com.riskanalyzer.entity.ModuleSkill;
import com.riskanalyzer.entity.Project;
import com.riskanalyzer.enums.Proficiency;
import com.riskanalyzer.exception.ResourceNotFoundException;
import com.riskanalyzer.repository.ModuleOwnerRepository;
import com.riskanalyzer.repository.ModuleRepository;
import com.riskanalyzer.repository.ModuleSkillRepository;
import com.riskanalyzer.repository.ProjectRepository;
import com.riskanalyzer.service.SkillGapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SkillGapServiceImpl implements SkillGapService {

    private final ModuleRepository moduleRepository;
    private final ProjectRepository projectRepository;
    private final ModuleSkillRepository moduleSkillRepository;
    private final ModuleOwnerRepository moduleOwnerRepository;

    @Override
    public List<SkillGapResponse> analyzeModuleSkillGaps(Long moduleId) {

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Module not found: " + moduleId));

        List<ModuleSkill> requiredSkills =
                moduleSkillRepository.findByModuleId(moduleId);

        List<ModuleOwner> activeOwners =
                moduleOwnerRepository.findByModuleIdAndIsActiveTrue(moduleId);

        List<SkillGapResponse> gaps = new ArrayList<>();

        for (ModuleSkill moduleSkill : requiredSkills) {

            String skillName = moduleSkill.getSkill().getName();
            Proficiency requiredLevel = moduleSkill.getRequiredLevel();

            // Find owners who have this skill at required level or above
            List<String> qualifiedEmployees = activeOwners.stream()
                    .filter(owner -> ownerHasSkillAtLevel(
                            owner, moduleSkill, requiredLevel))
                    .map(owner -> owner.getEmployee().getFullName())
                    .collect(Collectors.toList());

            boolean hasGap = qualifiedEmployees.isEmpty();
            boolean insufficientCoverage = qualifiedEmployees.size() < 2;

            String gapReason = buildGapReason(
                    hasGap, insufficientCoverage,
                    qualifiedEmployees.size(), skillName, requiredLevel);

            gaps.add(SkillGapResponse.builder()
                    .moduleId(moduleId)
                    .moduleName(module.getName())
                    .projectName(module.getProject().getName())
                    .skillName(skillName)
                    .requiredLevel(requiredLevel)
                    .availableEmployeeCount(qualifiedEmployees.size())
                    .availableEmployees(qualifiedEmployees)
                    .hasGap(hasGap || insufficientCoverage)
                    .gapReason(gapReason)
                    .build());
        }

        return gaps;
    }

    @Override
    public ProjectSkillGapResponse analyzeProjectSkillGaps(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Project not found: " + projectId));

        List<Module> modules = moduleRepository.findByProjectId(projectId);

        List<SkillGapResponse> allGaps = new ArrayList<>();

        for (Module module : modules) {
            List<SkillGapResponse> moduleGaps =
                    analyzeModuleSkillGaps(module.getId());
            allGaps.addAll(moduleGaps);
        }

        long totalGaps = allGaps.stream()
                .filter(SkillGapResponse::isHasGap)
                .count();

        return ProjectSkillGapResponse.builder()
                .projectId(projectId)
                .projectName(project.getName())
                .totalSkillsRequired(allGaps.size())
                .totalGapsFound((int) totalGaps)
                .skillGaps(allGaps)
                .build();
    }

    @Override
    public List<SkillGapResponse> getAllSkillGaps() {
        return moduleRepository.findAll()
                .stream()
                .flatMap(m -> analyzeModuleSkillGaps(m.getId()).stream())
                .filter(SkillGapResponse::isHasGap)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────

    private boolean ownerHasSkillAtLevel(ModuleOwner owner,
                                         ModuleSkill moduleSkill,
                                         Proficiency requiredLevel) {
        return owner.getEmployee()
                .getEmployeeSkills()
                .stream()
                .anyMatch(es ->
                        es.getSkill().getId()
                                .equals(moduleSkill.getSkill().getId())
                                && isProficiencyMet(es.getProficiency(), requiredLevel)
                );
    }

    private boolean isProficiencyMet(Proficiency actual,
                                     Proficiency required) {
        // EXPERT >= INTERMEDIATE >= BEGINNER
        return actual.ordinal() >= required.ordinal();
    }

    private String buildGapReason(boolean noEmployee,
                                  boolean insufficientCoverage,
                                  int count,
                                  String skillName,
                                  Proficiency requiredLevel) {
        if (noEmployee) {
            return "No owner has " + skillName
                    + " at " + requiredLevel + " level or above";
        }
        if (insufficientCoverage) {
            return "Only " + count + " owner has " + skillName
                    + " — recommend at least 2 for coverage";
        }
        return "Adequate coverage with " + count + " qualified owners";
    }
}