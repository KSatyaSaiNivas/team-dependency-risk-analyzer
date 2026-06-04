package com.riskanalyzer.entity;

import com.riskanalyzer.enums.EmployeeStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

    @Entity
    @Table(name = "employees")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Employee {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "full_name", nullable = false, length = 150)
        private String fullName;

        @Column(nullable = false, unique = true, length = 150)
        private String email;

        @Column(length = 100)
        private String department;

        @Column(length = 100)
        private String designation;

        @Column(name = "joining_date")
        private LocalDate joiningDate;

        @Enumerated(EnumType.STRING)
        @Column(length = 20)
        private EmployeeStatus status = EmployeeStatus.ACTIVE;

        @OneToOne
        @JoinColumn(name = "user_id", unique = true)
        private User user;

        @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private List<EmployeeSkill> employeeSkills = new ArrayList<>();

        @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private List<ModuleOwner> moduleOwnerships = new ArrayList<>();

        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;
    }

