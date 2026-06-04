package com.riskanalyzer.entity;


import com.riskanalyzer.enums.CriticalityLevel;
import com.riskanalyzer.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

    @Entity
    @Table(name = "projects")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Project {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 200)
        private String name;

        @Column(columnDefinition = "TEXT")
        private String description;

        @Column(name = "start_date")
        private LocalDate startDate;

        @Enumerated(EnumType.STRING)
        @Column(length = 20)
        private ProjectStatus status = ProjectStatus.ACTIVE;

        @Enumerated(EnumType.STRING)
        @Column(length = 20)
        private CriticalityLevel criticality = CriticalityLevel.MEDIUM;

        @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private List<Module> modules = new ArrayList<>();

        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;
    }

