package com.riskanalyzer.repository;

import com.riskanalyzer.entity.Project;
import com.riskanalyzer.enums.CriticalityLevel;
import com.riskanalyzer.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByStatus(ProjectStatus status);

    List<Project> findByCriticality(CriticalityLevel criticality);

    boolean existsByName(String name);
}