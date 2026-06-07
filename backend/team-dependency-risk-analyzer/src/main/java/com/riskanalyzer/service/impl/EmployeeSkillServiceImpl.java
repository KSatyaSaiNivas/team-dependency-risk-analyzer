package com.riskanalyzer.service.impl;

import com.riskanalyzer.dto.request.AssignSkillRequest;
import com.riskanalyzer.dto.response.SkillResponse;
import com.riskanalyzer.entity.Employee;
import com.riskanalyzer.entity.EmployeeSkill;
import com.riskanalyzer.entity.Skill;
import com.riskanalyzer.exception.ResourceNotFoundException;
import com.riskanalyzer.repository.EmployeeRepository;
import com.riskanalyzer.repository.EmployeeSkillRepository;
import com.riskanalyzer.repository.SkillRepository;
import com.riskanalyzer.service.EmployeeSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeSkillServiceImpl implements EmployeeSkillService {

    private final EmployeeSkillRepository employeeSkillRepository;
    private final EmployeeRepository employeeRepository;
    private final SkillRepository skillRepository;

    @Override
    public void assignSkillToEmployee(AssignSkillRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found: " + request.getEmployeeId()));

        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Skill not found: " + request.getSkillId()));

        if (employeeSkillRepository.existsByEmployeeIdAndSkillId(
                request.getEmployeeId(), request.getSkillId())) {
            throw new RuntimeException("Skill already assigned to employee");
        }

        EmployeeSkill employeeSkill = EmployeeSkill.builder()
                .employee(employee)
                .skill(skill)
                .proficiency(request.getProficiency())
                .build();

        employeeSkillRepository.save(employeeSkill);
    }

    @Override
    public List<SkillResponse> getSkillsByEmployee(Long employeeId) {
        return employeeSkillRepository.findByEmployeeId(employeeId)
                .stream()
                .map(es -> SkillResponse.builder()
                        .id(es.getSkill().getId())
                        .name(es.getSkill().getName())
                        .category(es.getSkill().getCategory())
                        .criticality(es.getSkill().getCriticality())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void removeSkillFromEmployee(Long employeeId, Long skillId) {
        EmployeeSkill es = employeeSkillRepository
                .findByEmployeeIdAndSkillId(employeeId, skillId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Skill assignment not found"));
        employeeSkillRepository.delete(es);
    }
}