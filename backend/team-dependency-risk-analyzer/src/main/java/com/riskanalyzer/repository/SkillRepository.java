package com.riskanalyzer.repository;

import com.riskanalyzer.entity.Skill;
import com.riskanalyzer.enums.SkillCategory;
import com.riskanalyzer.enums.SkillCriticality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    Optional<Skill> findByName(String name);

    boolean existsByName(String name);

    List<Skill> findByCategory(SkillCategory category);

    List<Skill> findByCriticality(SkillCriticality criticality);
}