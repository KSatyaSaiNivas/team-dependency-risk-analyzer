package com.riskanalyzer.repository;

import com.riskanalyzer.entity.ModuleSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleSkillRepository extends JpaRepository<ModuleSkill, Long> {

    List<ModuleSkill> findByModuleId(Long moduleId);

    List<ModuleSkill> findBySkillId(Long skillId);

    Optional<ModuleSkill> findByModuleIdAndSkillId(Long moduleId, Long skillId);

    boolean existsByModuleIdAndSkillId(Long moduleId, Long skillId);
}