package com.riskanalyzer.entity;

import com.riskanalyzer.enums.Proficiency;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

    @Entity
    @Table(name = "employee_skills",
            uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "skill_id"}))
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class EmployeeSkill {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "employee_id", nullable = false)
        private Employee employee;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "skill_id", nullable = false)
        private Skill skill;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 20)
        private Proficiency proficiency;

        @CreationTimestamp
        @Column(name = "assigned_at", updatable = false)
        private LocalDateTime assignedAt;

}
