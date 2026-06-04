package com.riskanalyzer.repository;

import com.riskanalyzer.entity.Module;
import com.riskanalyzer.enums.CriticalityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    List<Module> findByProjectId(Long projectId);

    List<Module> findByCriticality(CriticalityLevel criticality);

    @Query("SELECT m FROM Module m WHERE m.project.id = :projectId AND m.criticality = :criticality")
    List<Module> findByProjectIdAndCriticality(
            @Param("projectId") Long projectId,
            @Param("criticality") CriticalityLevel criticality
    );
}