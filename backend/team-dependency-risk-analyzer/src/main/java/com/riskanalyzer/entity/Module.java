package com.riskanalyzer.entity;

import com.riskanalyzer.enums.CriticalityLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

    @Entity
    @Table(name = "modules")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Module {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "project_id", nullable = false)
        private Project project;

        @Column(nullable = false, length = 200)
        private String name;

        @Column(columnDefinition = "TEXT")
        private String description;

        @Enumerated(EnumType.STRING)
        @Column(length = 20)
        private CriticalityLevel criticality = CriticalityLevel.MEDIUM;

        @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private List<ModuleOwner> owners = new ArrayList<>();

        @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private List<ModuleSkill> requiredSkills = new ArrayList<>();

        @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private List<RiskScore> riskScores = new ArrayList<>();

        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;
    }
