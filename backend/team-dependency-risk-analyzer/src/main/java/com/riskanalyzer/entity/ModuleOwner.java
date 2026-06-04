package com.riskanalyzer.entity;

import com.riskanalyzer.enums.OwnershipType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "module_owners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "ownership_type", nullable = false, length = 20)
    private OwnershipType ownershipType;

    @Column(name = "assigned_date")
    private LocalDate assignedDate;

    @Column(name = "relieved_date")
    private LocalDate relievedDate;

    @Column(name = "is_active")
    private Boolean isActive = true;
}