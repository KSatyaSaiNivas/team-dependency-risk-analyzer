package com.riskanalyzer.service.impl;

import com.riskanalyzer.dto.response.ExitSimulationResponse;
import com.riskanalyzer.entity.Employee;
import com.riskanalyzer.entity.EmployeeSkill;
import com.riskanalyzer.entity.ModuleOwner;
import com.riskanalyzer.enums.RiskLevel;
import com.riskanalyzer.exception.ResourceNotFoundException;
import com.riskanalyzer.repository.EmployeeRepository;
import com.riskanalyzer.repository.EmployeeSkillRepository;
import com.riskanalyzer.repository.ModuleOwnerRepository;
import com.riskanalyzer.service.ExitSimulationService;
import com.riskanalyzer.service.RiskAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExitSimulationServiceImpl implements ExitSimulationService {

    private final EmployeeRepository employeeRepository;
    private final ModuleOwnerRepository moduleOwnerRepository;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final RiskAnalysisService riskAnalysisService;

    @Override
    public ExitSimulationResponse simulateExit(Long employeeId, Long projectId) {

        // Step 1: Find the employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found: " + employeeId));

        // Step 2: Find all active modules this employee owns
        List<ModuleOwner> ownedModules =
                moduleOwnerRepository.findByEmployeeIdAndIsActiveTrue(employeeId);

        List<String> affectedModuleNames = ownedModules.stream()
                .map(mo -> mo.getModule().getName())
                .collect(Collectors.toList());

        // Step 3: Find orphaned modules
        // (modules that would have ZERO owners if this employee leaves)
        List<String> orphanedModules = new ArrayList<>();
        for (ModuleOwner ownership : ownedModules) {
            Long moduleId = ownership.getModule().getId();
            long remainingOwners = moduleOwnerRepository
                    .findByModuleIdAndIsActiveTrue(moduleId)
                    .stream()
                    .filter(mo -> !mo.getEmployee().getId().equals(employeeId))
                    .count();

            if (remainingOwners == 0) {
                orphanedModules.add(ownership.getModule().getName());
            }
        }

        // Step 4: Find skills this employee has that no other active owner has
        List<EmployeeSkill> employeeSkills =
                employeeSkillRepository.findByEmployeeId(employeeId);

        List<String> skillsMissing = new ArrayList<>();
        List<String> skillsRare = new ArrayList<>();

        for (EmployeeSkill es : employeeSkills) {
            Long skillId = es.getSkill().getId();

            // Count other ACTIVE employees who have this skill
            long otherEmployeesWithSkill = employeeSkillRepository
                    .findActiveEmployeesBySkillId(skillId)
                    .stream()
                    .filter(other -> !other.getEmployee().getId()
                            .equals(employeeId))
                    .count();

            if (otherEmployeesWithSkill == 0) {
                skillsMissing.add(es.getSkill().getName()
                        + " (" + es.getProficiency() + ")");
            } else if (otherEmployeesWithSkill == 1) {
                skillsRare.add(es.getSkill().getName()
                        + " (only 1 person remaining)");
            }
        }

        // Step 5: Calculate risk before and after
        RiskLevel riskBefore = calculateProjectRiskLevel(projectId);
        RiskLevel riskAfter = estimateRiskAfterExit(
                riskBefore, orphanedModules.size(), skillsMissing.size());

        // Step 6: Build recommendations
        List<String> recommendations = buildRecommendations(
                orphanedModules, skillsMissing, skillsRare, employee);

        // Step 7: Overall impact statement
        String overallImpact = buildImpactStatement(
                affectedModuleNames.size(),
                orphanedModules.size(),
                skillsMissing.size(),
                employee.getFullName());

        return ExitSimulationResponse.builder()
                .employeeId(employeeId)
                .employeeName(employee.getFullName())
                .department(employee.getDepartment())
                .totalModulesOwned(ownedModules.size())
                .affectedModules(affectedModuleNames)
                .orphanedModules(orphanedModules)
                .skillsThatBecomeMissing(skillsMissing)
                .skillsThatBecomeRare(skillsRare)
                .riskBefore(riskBefore)
                .riskAfter(riskAfter)
                .overallImpact(overallImpact)
                .recommendations(recommendations)
                .build();
    }

    // ─────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────

    private RiskLevel calculateProjectRiskLevel(Long projectId) {
        try {
            var summary = riskAnalysisService.calculateProjectRisk(projectId);
            return summary.getOverallRiskLevel();
        } catch (Exception e) {
            return RiskLevel.MEDIUM;
        }
    }

    private RiskLevel estimateRiskAfterExit(RiskLevel current,
                                            int orphanedCount,
                                            int missingSkillsCount) {
        int riskIndex = current.ordinal();

        if (orphanedCount > 0) riskIndex += 2;
        else if (missingSkillsCount > 0) riskIndex += 1;

        riskIndex = Math.min(riskIndex, RiskLevel.values().length - 1);
        return RiskLevel.values()[riskIndex];
    }

    private List<String> buildRecommendations(List<String> orphaned,
                                              List<String> missing,
                                              List<String> rare,
                                              Employee employee) {
        List<String> recommendations = new ArrayList<>();

        if (!orphaned.isEmpty()) {
            recommendations.add("URGENT: Assign new owners immediately to: "
                    + String.join(", ", orphaned));
        }

        if (!missing.isEmpty()) {
            recommendations.add("Start training another employee on: "
                    + String.join(", ", missing));
        }

        if (!rare.isEmpty()) {
            recommendations.add("Skills becoming rare after exit: "
                    + String.join(", ", rare)
                    + " — prioritize knowledge transfer");
        }

        if (orphaned.isEmpty() && missing.isEmpty()) {
            recommendations.add("Low impact exit — team has adequate coverage");
        }

        recommendations.add("Schedule knowledge transfer sessions with "
                + employee.getFullName() + " before last working day");

        return recommendations;
    }

    private String buildImpactStatement(int affected,
                                        int orphaned,
                                        int missingSkills,
                                        String name) {
        if (orphaned > 0 || missingSkills > 2) {
            return "HIGH IMPACT: Exit of " + name
                    + " will critically affect " + affected
                    + " modules and leave " + orphaned
                    + " modules without any owner";
        } else if (affected > 0 || missingSkills > 0) {
            return "MEDIUM IMPACT: Exit of " + name
                    + " will affect " + affected
                    + " modules with some skill gaps";
        } else {
            return "LOW IMPACT: Exit of " + name
                    + " has minimal effect on project continuity";
        }
    }
}