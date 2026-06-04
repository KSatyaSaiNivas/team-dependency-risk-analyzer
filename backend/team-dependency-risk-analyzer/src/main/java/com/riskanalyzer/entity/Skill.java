package com.riskanalyzer.entity;

import com.riskanalyzer.enums.SkillCategory;
import com.riskanalyzer.enums.SkillCriticality;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

    @Entity
    @Table(name = "skills")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Skill {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true, length = 100)
        private String name;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 20)
        private SkillCategory category;

        @Enumerated(EnumType.STRING)
        @Column(length = 20)
        private SkillCriticality criticality = SkillCriticality.COMMON;

        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;
    }
