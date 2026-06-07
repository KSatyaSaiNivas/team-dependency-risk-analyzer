package com.riskanalyzer.service.impl;

import com.riskanalyzer.dto.response.DashboardSummaryResponse;
import com.riskanalyzer.dto.response.ModuleRiskResponse;
import com.riskanalyzer.dto.response.ProjectRiskSummaryResponse;
import com.riskanalyzer.entity.Module;
import com.riskanalyzer.enums.RiskLevel;
import com.riskanalyzer.repository.*;
import com.riskanalyzer.service.DashboardService;
import com.riskanalyzer.service.RiskAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DashboardServiceImpl implements DashboardService {

    private final ProjectRepository projectRepository;
    private final ModuleRepository moduleRepository;
    private final EmployeeRepository employeeRepository;
    private final SkillRepository skillRepository;
    private final ModuleOwnerRepository moduleOwnerRepository;
    private final RiskAnalysisService riskAnalysisService;

    @Override
    public DashboardSummaryResponse getDashboardSummary() {

        // Step 1: Basic counts
        int totalProjects   = (int) projectRepository.count();
        int totalModules    = (int) moduleRepository.count();
        int totalEmployees  = (int) employeeRepository.count();
        int totalSkills     = (int) skillRepository.count();

        // Step 2: Calculate risk for all modules
        List<Module> allModules = moduleRepository.findAll();

        List<ModuleRiskResponse> allModuleRisks = allModules.stream()
                .map(m -> riskAnalysisService.calculateModuleRisk(m.getId()))
                .collect(Collectors.toList());

        // Step 3: Count by risk level
        long critical = allModuleRisks.stream()
                .filter(m -> m.getRiskLevel() == RiskLevel.CRITICAL).count();
        long high = allModuleRisks.stream()
                .filter(m -> m.getRiskLevel() == RiskLevel.HIGH).count();
        long medium = allModuleRisks.stream()
                .filter(m -> m.getRiskLevel() == RiskLevel.MEDIUM).count();
        long low = allModuleRisks.stream()
                .filter(m -> m.getRiskLevel() == RiskLevel.LOW).count();

        // Step 4: Count orphaned modules (0 owners)
        long orphaned = allModules.stream()
                .filter(m -> moduleOwnerRepository
                        .findByModuleIdAndIsActiveTrue(m.getId()).isEmpty())
                .count();

        // Step 5: Count single owner modules
        long singleOwner = allModules.stream()
                .filter(m -> moduleOwnerRepository
                        .countActiveOwnersByModuleId(m.getId()) == 1)
                .count();

        // Step 6: Top 5 high risk modules
        List<ModuleRiskResponse> top5 = allModuleRisks.stream()
                .sorted(Comparator.comparing(ModuleRiskResponse::getRiskScore)
                        .reversed())
                .limit(5)
                .collect(Collectors.toList());

        // Step 7: Per project risk summaries
        List<ProjectRiskSummaryResponse> projectSummaries =
                projectRepository.findAll().stream()
                        .map(p -> riskAnalysisService
                                .calculateProjectRisk(p.getId()))
                        .collect(Collectors.toList());

        // Step 8: Overall system risk
        RiskLevel overallRisk = RiskLevel.LOW;
        if (critical > 0)     overallRisk = RiskLevel.CRITICAL;
        else if (high > 0)    overallRisk = RiskLevel.HIGH;
        else if (medium > 0)  overallRisk = RiskLevel.MEDIUM;

        return DashboardSummaryResponse.builder()
                .totalProjects(totalProjects)
                .totalModules(totalModules)
                .totalEmployees(totalEmployees)
                .totalSkills(totalSkills)
                .criticalRiskModules(critical)
                .highRiskModules(high)
                .mediumRiskModules(medium)
                .lowRiskModules(low)
                .orphanedModules(orphaned)
                .singleOwnerModules(singleOwner)
                .overallSystemRisk(overallRisk)
                .top5HighRiskModules(top5)
                .projectRiskSummaries(projectSummaries)
                .build();
    }
}