package com.riskanalyzer.entity;

import com.riskanalyzer.enums.Proficiency;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "module_skills",
        uniqueConstraints = @UniqueConstraint(columnNames = {"module_id", "skill_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(name = "required_level", length = 20)
    private Proficiency requiredLevel = Proficiency.INTERMEDIATE;
}