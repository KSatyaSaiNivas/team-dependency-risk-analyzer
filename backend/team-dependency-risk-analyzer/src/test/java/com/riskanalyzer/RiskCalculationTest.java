package com.riskanalyzer;

import com.riskanalyzer.dto.response.ModuleRiskResponse;
import com.riskanalyzer.dto.response.ProjectRiskSummaryResponse;
import com.riskanalyzer.entity.Employee;
import com.riskanalyzer.entity.Module;          // ← ADD THIS explicitly
import com.riskanalyzer.entity.ModuleOwner;
import com.riskanalyzer.entity.ModuleSkill;
import com.riskanalyzer.entity.Project;
import com.riskanalyzer.entity.RiskScore;
import com.riskanalyzer.entity.Skill;
import com.riskanalyzer.enums.CriticalityLevel;
import com.riskanalyzer.enums.EmployeeStatus;
import com.riskanalyzer.enums.OwnershipType;
import com.riskanalyzer.enums.Proficiency;
import com.riskanalyzer.enums.ProjectStatus;
import com.riskanalyzer.enums.RiskLevel;
import com.riskanalyzer.enums.SkillCriticality;
import com.riskanalyzer.exception.ResourceNotFoundException;
import com.riskanalyzer.repository.ModuleOwnerRepository;
import com.riskanalyzer.repository.ModuleRepository;
import com.riskanalyzer.repository.ModuleSkillRepository;
import com.riskanalyzer.repository.ProjectRepository;
import com.riskanalyzer.repository.RiskScoreRepository;
import com.riskanalyzer.service.impl.RiskAnalysisServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class RiskCalculationTest {

    @Mock private ModuleRepository moduleRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private ModuleOwnerRepository moduleOwnerRepository;
    @Mock private ModuleSkillRepository moduleSkillRepository;
    @Mock private RiskScoreRepository riskScoreRepository;

    @InjectMocks
    private RiskAnalysisServiceImpl riskAnalysisService;

    private Project project;
    private Module module;
    private Employee employee1;
    private Employee employee2;
    private Employee employee3;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setStatus(ProjectStatus.ACTIVE);
        project.setCriticality(CriticalityLevel.HIGH);

        module = new Module();
        module.setId(1L);
        module.setName("Payment Module");
        module.setProject(project);
        module.setCriticality(CriticalityLevel.CRITICAL);

        employee1 = new Employee();
        employee1.setId(1L);
        employee1.setFullName("Ravi Kumar");
        employee1.setStatus(EmployeeStatus.ACTIVE);

        employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFullName("Priya Sharma");
        employee2.setStatus(EmployeeStatus.ACTIVE);

        employee3 = new Employee();
        employee3.setId(3L);
        employee3.setFullName("Amit Singh");
        employee3.setStatus(EmployeeStatus.ACTIVE);
    }

    // ─────────────────────────────────────────
    // TEST 1: Single owner = CRITICAL risk
    // ─────────────────────────────────────────
    @Test
    void whenSingleOwnerAndCriticalModule_thenRiskIsCritical() {
        ModuleOwner owner = new ModuleOwner();
        owner.setEmployee(employee1);
        owner.setModule(module);
        owner.setOwnershipType(OwnershipType.PRIMARY);
        owner.setIsActive(true);

        when(moduleRepository.findById(1L))
                .thenReturn(Optional.of(module));
        when(moduleOwnerRepository.findByModuleIdAndIsActiveTrue(1L))
                .thenReturn(List.of(owner));
        when(moduleSkillRepository.findByModuleId(1L))
                .thenReturn(List.of());
        when(riskScoreRepository.save(any()))
                .thenReturn(null);

        ModuleRiskResponse result =
                riskAnalysisService.calculateModuleRisk(1L);

        assertEquals(RiskLevel.CRITICAL, result.getRiskLevel());
        assertEquals(1, result.getOwnerCount());
        assertTrue(result.getRiskScore().doubleValue() > 80);
    }

    // ─────────────────────────────────────────
    // TEST 2: Two owners = HIGH risk
    // ─────────────────────────────────────────
    @Test
    void whenTwoOwnersAndHighModule_thenRiskIsHigh() {
        module.setCriticality(CriticalityLevel.HIGH);

        ModuleOwner owner1 = new ModuleOwner();
        owner1.setEmployee(employee1);
        owner1.setModule(module);
        owner1.setIsActive(true);

        ModuleOwner owner2 = new ModuleOwner();
        owner2.setEmployee(employee2);
        owner2.setModule(module);
        owner2.setIsActive(true);

        when(moduleRepository.findById(1L))
                .thenReturn(Optional.of(module));
        when(moduleOwnerRepository.findByModuleIdAndIsActiveTrue(1L))
                .thenReturn(List.of(owner1, owner2));
        when(moduleSkillRepository.findByModuleId(1L))
                .thenReturn(List.of());
        when(riskScoreRepository.save(any()))
                .thenReturn(null);

        ModuleRiskResponse result =
                riskAnalysisService.calculateModuleRisk(1L);

        assertEquals(RiskLevel.HIGH, result.getRiskLevel());
        assertEquals(2, result.getOwnerCount());
        assertTrue(result.getRiskScore().doubleValue() > 60);
    }

    // ─────────────────────────────────────────
    // TEST 3: Three+ owners = LOW risk
    // ─────────────────────────────────────────
    @Test
    void whenThreeOwnersAndLowModule_thenRiskIsLow() {
        module.setCriticality(CriticalityLevel.LOW);

        ModuleOwner owner1 = new ModuleOwner();
        owner1.setEmployee(employee1);
        owner1.setIsActive(true);

        ModuleOwner owner2 = new ModuleOwner();
        owner2.setEmployee(employee2);
        owner2.setIsActive(true);

        ModuleOwner owner3 = new ModuleOwner();
        owner3.setEmployee(employee3);
        owner3.setIsActive(true);

        when(moduleRepository.findById(1L))
                .thenReturn(Optional.of(module));
        when(moduleOwnerRepository.findByModuleIdAndIsActiveTrue(1L))
                .thenReturn(List.of(owner1, owner2, owner3));
        when(moduleSkillRepository.findByModuleId(1L))
                .thenReturn(List.of());
        when(riskScoreRepository.save(any()))
                .thenReturn(null);

        ModuleRiskResponse result =
                riskAnalysisService.calculateModuleRisk(1L);

        assertEquals(RiskLevel.LOW, result.getRiskLevel());
        assertEquals(3, result.getOwnerCount());
        assertTrue(result.getRiskScore().doubleValue() <= 40);
    }

    // ─────────────────────────────────────────
    // TEST 4: No owners = CRITICAL
    // ─────────────────────────────────────────
    @Test
    void whenNoOwners_thenRiskIsCritical() {
        when(moduleRepository.findById(1L))
                .thenReturn(Optional.of(module));
        when(moduleOwnerRepository.findByModuleIdAndIsActiveTrue(1L))
                .thenReturn(List.of());
        when(moduleSkillRepository.findByModuleId(1L))
                .thenReturn(List.of());
        when(riskScoreRepository.save(any()))
                .thenReturn(null);

        ModuleRiskResponse result =
                riskAnalysisService.calculateModuleRisk(1L);

        assertEquals(RiskLevel.CRITICAL, result.getRiskLevel());
        assertEquals(0, result.getOwnerCount());
    }

    // ─────────────────────────────────────────
    // TEST 5: Rare skill increases risk score
    // ─────────────────────────────────────────
    @Test
    void whenRareSkillRequired_thenRiskScoreIncreases() {
        module.setCriticality(CriticalityLevel.LOW);

        ModuleOwner owner1 = new ModuleOwner();
        owner1.setEmployee(employee1);
        owner1.setIsActive(true);

        ModuleOwner owner2 = new ModuleOwner();
        owner2.setEmployee(employee2);
        owner2.setIsActive(true);

        ModuleOwner owner3 = new ModuleOwner();
        owner3.setEmployee(employee3);
        owner3.setIsActive(true);

        Skill rareSkill = new Skill();
        rareSkill.setId(1L);
        rareSkill.setName("Blockchain");
        rareSkill.setCriticality(SkillCriticality.RARE);

        ModuleSkill moduleSkill = new ModuleSkill();
        moduleSkill.setSkill(rareSkill);
        moduleSkill.setRequiredLevel(Proficiency.EXPERT);

        when(moduleRepository.findById(1L))
                .thenReturn(Optional.of(module));
        when(moduleOwnerRepository.findByModuleIdAndIsActiveTrue(1L))
                .thenReturn(List.of(owner1, owner2, owner3));
        when(moduleSkillRepository.findByModuleId(1L))
                .thenReturn(List.of(moduleSkill));
        when(riskScoreRepository.save(any()))
                .thenReturn(null);

        ModuleRiskResponse result =
                riskAnalysisService.calculateModuleRisk(1L);

        assertTrue(result.getRiskScore().doubleValue() > 20);
        assertTrue(result.getRareSkills().contains("Blockchain"));
    }

    // ─────────────────────────────────────────
    // TEST 6: Module not found throws exception
    // ─────────────────────────────────────────
    @Test
    void whenModuleNotFound_thenThrowException() {
        when(moduleRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                riskAnalysisService.calculateModuleRisk(999L));
    }

    // ─────────────────────────────────────────
    // TEST 7: Project risk summary
    // ─────────────────────────────────────────
    @Test
    void whenProjectHasModules_thenSummaryIsCorrect() {
        ModuleOwner owner = new ModuleOwner();
        owner.setEmployee(employee1);
        owner.setModule(module);
        owner.setIsActive(true);

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));
        when(moduleRepository.findByProjectId(1L))
                .thenReturn(List.of(module));
        when(moduleRepository.findById(1L))
                .thenReturn(Optional.of(module));
        when(moduleOwnerRepository.findByModuleIdAndIsActiveTrue(1L))
                .thenReturn(List.of(owner));
        when(moduleSkillRepository.findByModuleId(1L))
                .thenReturn(List.of());
        when(riskScoreRepository.save(any()))
                .thenReturn(null);

        ProjectRiskSummaryResponse result =
                riskAnalysisService.calculateProjectRisk(1L);

        assertEquals(1, result.getTotalModules());
        assertNotNull(result.getOverallRiskLevel());
        assertEquals("Test Project", result.getProjectName());
    }
}