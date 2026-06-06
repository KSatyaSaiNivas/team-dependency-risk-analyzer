package com.riskanalyzer.service.impl;

import com.riskanalyzer.dto.request.CreateSkillRequest;
import com.riskanalyzer.dto.response.SkillResponse;
import com.riskanalyzer.entity.Skill;
import com.riskanalyzer.exception.ResourceNotFoundException;
import com.riskanalyzer.repository.SkillRepository;
import com.riskanalyzer.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    @Override
    public SkillResponse createSkill(CreateSkillRequest request) {
        if (skillRepository.existsByName(request.getName())) {
            throw new RuntimeException("Skill already exists: " + request.getName());
        }
        Skill skill = Skill.builder()
                .name(request.getName())
                .category(request.getCategory())
                .criticality(request.getCriticality())
                .build();
        return mapToResponse(skillRepository.save(skill));
    }

    @Override
    public SkillResponse getSkillById(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + id));
        return mapToResponse(skill);
    }

    @Override
    public List<SkillResponse> getAllSkills() {
        return skillRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SkillResponse updateSkill(Long id, CreateSkillRequest request) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + id));
        skill.setName(request.getName());
        skill.setCategory(request.getCategory());
        skill.setCriticality(request.getCriticality());
        return mapToResponse(skillRepository.save(skill));
    }

    @Override
    public void deleteSkill(Long id) {
        if (!skillRepository.existsById(id)) {
            throw new ResourceNotFoundException("Skill not found with id: " + id);
        }
        skillRepository.deleteById(id);
    }

    private SkillResponse mapToResponse(Skill skill) {
        return SkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .category(skill.getCategory())
                .criticality(skill.getCriticality())
                .build();
    }
}