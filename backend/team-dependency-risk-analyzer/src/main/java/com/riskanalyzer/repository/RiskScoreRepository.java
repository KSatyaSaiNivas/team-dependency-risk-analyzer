package com.riskanalyzer.repository;

import com.riskanalyzer.entity.RiskScore;
import com.riskanalyzer.enums.RiskLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RiskScoreRepository extends JpaRepository<RiskScore, Long> {

    List<RiskScore> findByModuleId(Long moduleId);

    List<RiskScore> findByRiskLevel(RiskLevel riskLevel);

    @Query("SELECT rs FROM RiskScore rs WHERE rs.module.id = :moduleId ORDER BY rs.calculatedAt DESC")
    Optional<RiskScore> findLatestByModuleId(@Param("moduleId") Long moduleId);

    @Query("SELECT rs FROM RiskScore rs WHERE rs.module.project.id = :projectId ORDER BY rs.calculatedAt DESC")
    List<RiskScore> findLatestByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT rs FROM RiskScore rs WHERE rs.riskLevel IN ('HIGH', 'CRITICAL') ORDER BY rs.riskScore DESC")
    List<RiskScore> findAllHighAndCriticalRisks();
}