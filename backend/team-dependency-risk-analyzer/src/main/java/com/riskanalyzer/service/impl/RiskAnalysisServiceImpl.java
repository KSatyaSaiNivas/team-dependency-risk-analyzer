package com.riskanalyzer.service.impl;


import org.springframework.transaction.annotation.Transactional;
import com.riskanalyzer.dto.response.ModuleRiskResponse;
import com.riskanalyzer.dto.response.ProjectRiskSummaryResponse;
import com.riskanalyzer.entity.ModuleOwner;
import com.riskanalyzer.entity.ModuleSkill;
import com.riskanalyzer.entity.Project;
import com.riskanalyzer.entity.RiskScore;
import com.riskanalyzer.entity.Module;
import com.riskanalyzer.enums.CriticalityLevel;
import com.riskanalyzer.enums.RiskLevel;
import com.riskanalyzer.enums.SkillCriticality;
import com.riskanalyzer.exception.ResourceNotFoundException;
import com.riskanalyzer.repository.ModuleOwnerRepository;
import com.riskanalyzer.repository.ModuleRepository;
import com.riskanalyzer.repository.ModuleSkillRepository;
import com.riskanalyzer.repository.ProjectRepository;
import com.riskanalyzer.repository.RiskScoreRepository;
import com.riskanalyzer.service.RiskAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RiskAnalysisServiceImpl implements RiskAnalysisService {

    private final ModuleRepository moduleRepository;
    private final ProjectRepository projectRepository;
    private final ModuleOwnerRepository moduleOwnerRepository;
    private final ModuleSkillRepository moduleSkillRepository;
    private final RiskScoreRepository riskScoreRepository;

    // ─────────────────────────────────────────
    // CORE RISK CALCULATION FOR ONE MODULE
    // ─────────────────────────────────────────
    @Override
    @Transactional
    public ModuleRiskResponse calculateModuleRisk(Long moduleId) {

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Module not found: " + moduleId));

        // Step 1: Count active owners
        List<ModuleOwner> activeOwners =
                moduleOwnerRepository.findByModuleIdAndIsActiveTrue(moduleId);
        int ownerCount = activeOwners.size();

        // Step 2: Base score from owner count
        double baseScore = calculateBaseScore(ownerCount);

        // Step 3: Criticality multiplier
        double multiplier = getCriticalityMultiplier(module.getCriticality());

        // Step 4: Skill rarity bonus
        List<ModuleSkill> moduleSkills =
                moduleSkillRepository.findByModuleId(moduleId);
        double skillBonus = calculateSkillBonus(moduleSkills);

        // Step 5: Final score
        double finalScore = (baseScore * multiplier) + skillBonus;
        finalScore = Math.min(finalScore, 100.0); // cap at 100

        // Step 6: Determine risk level
        RiskLevel riskLevel = determineRiskLevel(finalScore);

        // Step 7: Build reason string
        String reason = buildRiskReason(ownerCount, module.getCriticality(),
                skillBonus, finalScore);

        // Step 8: Save to risk_scores table
        saveRiskScore(module, riskLevel,
                BigDecimal.valueOf(finalScore).setScale(2, RoundingMode.HALF_UP),
                reason);

        // Step 9: Build and return response
        List<String> ownerNames = activeOwners.stream()
                .map(o -> o.getEmployee().getFullName())
                .collect(Collectors.toList());

        List<String> rareSkills = moduleSkills.stream()
                .filter(ms -> ms.getSkill().getCriticality()
                        == SkillCriticality.RARE)
                .map(ms -> ms.getSkill().getName())
                .collect(Collectors.toList());

        return ModuleRiskResponse.builder()
                .moduleId(module.getId())
                .moduleName(module.getName())
                .moduleCriticality(module.getCriticality())
                .projectId(module.getProject().getId())
                .projectName(module.getProject().getName())
                .ownerCount(ownerCount)
                .ownerNames(ownerNames)
                .rareSkills(rareSkills)
                .riskScore(BigDecimal.valueOf(finalScore)
                        .setScale(2, RoundingMode.HALF_UP))
                .riskLevel(riskLevel)
                .riskReason(reason)
                .build();
    }

    // ─────────────────────────────────────────
    // RISK SUMMARY FOR ENTIRE PROJECT
    // ─────────────────────────────────────────
    @Override
    @Transactional
    public ProjectRiskSummaryResponse calculateProjectRisk(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Project not found: " + projectId));

        List<Module> modules = moduleRepository.findByProjectId(projectId);

        if (modules.isEmpty()) {
            return ProjectRiskSummaryResponse.builder()
                    .projectId(projectId)
                    .projectName(project.getName())
                    .totalModules(0)
                    .overallRiskLevel(RiskLevel.LOW)
                    .moduleRisks(new ArrayList<>())
                    .build();
        }

        // Calculate risk for each module
        List<ModuleRiskResponse> moduleRisks = modules.stream()
                .map(m -> calculateModuleRisk(m.getId()))
                .collect(Collectors.toList());

        // Count by risk level
        long critical = moduleRisks.stream()
                .filter(m -> m.getRiskLevel() == RiskLevel.CRITICAL).count();
        long high = moduleRisks.stream()
                .filter(m -> m.getRiskLevel() == RiskLevel.HIGH).count();
        long medium = moduleRisks.stream()
                .filter(m -> m.getRiskLevel() == RiskLevel.MEDIUM).count();
        long low = moduleRisks.stream()
                .filter(m -> m.getRiskLevel() == RiskLevel.LOW).count();

        // Overall project risk = worst module risk
        RiskLevel overallRisk = RiskLevel.LOW;
        if (critical > 0) overallRisk = RiskLevel.CRITICAL;
        else if (high > 0) overallRisk = RiskLevel.HIGH;
        else if (medium > 0) overallRisk = RiskLevel.MEDIUM;

        return ProjectRiskSummaryResponse.builder()
                .projectId(projectId)
                .projectName(project.getName())
                .totalModules(modules.size())
                .criticalRiskModules(critical)
                .highRiskModules(high)
                .mediumRiskModules(medium)
                .lowRiskModules(low)
                .overallRiskLevel(overallRisk)
                .moduleRisks(moduleRisks)
                .build();
    }

    // ─────────────────────────────────────────
    // GET ALL HIGH + CRITICAL RISK MODULES
    // ─────────────────────────────────────────
    @Override
    @Transactional
    public List<ModuleRiskResponse> getAllHighRiskModules() {
        List<Module> allModules = moduleRepository.findAll();

        return allModules.stream()
                .map(m -> calculateModuleRisk(m.getId()))
                .filter(r -> r.getRiskLevel() == RiskLevel.HIGH
                        || r.getRiskLevel() == RiskLevel.CRITICAL)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────
    // CALCULATE AND SAVE ALL MODULE RISKS
    // ─────────────────────────────────────────
    @Override
    @Transactional
    public List<ModuleRiskResponse> calculateAndSaveAllRisks() {
        return moduleRepository.findAll()
                .stream()
                .map(m -> calculateModuleRisk(m.getId()))
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────
    // PRIVATE HELPER METHODS
    // ─────────────────────────────────────────

    private double calculateBaseScore(int ownerCount) {
        if (ownerCount == 0) return 95.0;  // No owner = extreme risk
        if (ownerCount == 1) return 80.0;  // Single owner = high risk
        if (ownerCount == 2) return 50.0;  // Two owners = medium risk
        return 20.0;                        // 3+ owners = low risk
    }

    private double getCriticalityMultiplier(CriticalityLevel criticality) {
        return switch (criticality) {
            case CRITICAL -> 1.5;
            case HIGH     -> 1.3;
            case MEDIUM   -> 1.1;
            case LOW      -> 1.0;
        };
    }

    private double calculateSkillBonus(List<ModuleSkill> moduleSkills) {
        double bonus = 0.0;
        for (ModuleSkill ms : moduleSkills) {
            if (ms.getSkill().getCriticality() == SkillCriticality.RARE) {
                bonus += 15.0;
            } else if (ms.getSkill().getCriticality() == SkillCriticality.MODERATE) {
                bonus += 5.0;
            }
        }
        return bonus;
    }

    private RiskLevel determineRiskLevel(double score) {
        if (score > 80) return RiskLevel.CRITICAL;
        if (score > 60) return RiskLevel.HIGH;
        if (score > 40) return RiskLevel.MEDIUM;
        return RiskLevel.LOW;
    }

    private String buildRiskReason(int ownerCount,
                                   CriticalityLevel criticality,
                                   double skillBonus,
                                   double finalScore) {
        StringBuilder reason = new StringBuilder();
        reason.append("Owner count: ").append(ownerCount);

        if (ownerCount == 0)
            reason.append(" (No owner assigned - extreme risk)");
        else if (ownerCount == 1)
            reason.append(" (Single point of failure)");
        else if (ownerCount == 2)
            reason.append(" (Limited coverage)");
        else
            reason.append(" (Good coverage)");

        reason.append(". Module criticality: ").append(criticality);

        if (skillBonus > 0)
            reason.append(". Rare/critical skills detected (+")
                    .append(skillBonus).append(" bonus)");

        reason.append(". Final score: ").append(
                String.format("%.2f", finalScore));

        return reason.toString();
    }

    private void saveRiskScore(Module module, RiskLevel riskLevel,
                               BigDecimal score, String reason) {
        RiskScore riskScore = RiskScore.builder()
                .module(module)
                .riskLevel(riskLevel)
                .riskScore(score)
                .riskReason(reason)
                .build();
        riskScoreRepository.save(riskScore);
    }
}