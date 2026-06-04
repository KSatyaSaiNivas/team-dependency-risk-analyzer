package com.riskanalyzer.repository;


import com.riskanalyzer.entity.Employee;
import com.riskanalyzer.enums.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

    @Repository
    public interface EmployeeRepository extends JpaRepository<Employee, Long> {

        Optional<Employee> findByEmail(String email);

        boolean existsByEmail(String email);

        List<Employee> findByStatus(EmployeeStatus status);

        List<Employee> findByDepartment(String department);

        @Query("SELECT e FROM Employee e WHERE e.status = 'ACTIVE'")
        List<Employee> findAllActiveEmployees();

}
