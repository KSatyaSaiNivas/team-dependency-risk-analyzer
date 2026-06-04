package com.riskanalyzer.entity;

import com.riskanalyzer.enums.RiskLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "risk_scores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 20)
    private RiskLevel riskLevel;

    @Column(name = "risk_score", precision = 5, scale = 2)
    private BigDecimal riskScore;

    @Column(name = "risk_reason", columnDefinition = "TEXT")
    private String riskReason;

    @CreationTimestamp
    @Column(name = "calculated_at", updatable = false)
    private LocalDateTime calculatedAt;
}