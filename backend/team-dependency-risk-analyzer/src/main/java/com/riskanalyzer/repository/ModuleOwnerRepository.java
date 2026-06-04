package com.riskanalyzer.repository;

import com.riskanalyzer.entity.ModuleOwner;
import com.riskanalyzer.enums.OwnershipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleOwnerRepository extends JpaRepository<ModuleOwner, Long> {

    List<ModuleOwner> findByModuleId(Long moduleId);

    List<ModuleOwner> findByEmployeeId(Long employeeId);

    List<ModuleOwner> findByModuleIdAndIsActiveTrue(Long moduleId);

    List<ModuleOwner> findByEmployeeIdAndIsActiveTrue(Long employeeId);

    @Query("SELECT mo FROM ModuleOwner mo WHERE mo.module.id = :moduleId AND mo.isActive = true AND mo.ownershipType = :type")
    List<ModuleOwner> findActiveOwnersByModuleIdAndType(
            @Param("moduleId") Long moduleId,
            @Param("type") OwnershipType type
    );

    @Query("SELECT COUNT(mo) FROM ModuleOwner mo WHERE mo.module.id = :moduleId AND mo.isActive = true")
    long countActiveOwnersByModuleId(@Param("moduleId") Long moduleId);
}