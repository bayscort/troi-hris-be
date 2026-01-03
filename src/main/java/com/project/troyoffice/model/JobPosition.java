package com.project.troyoffice.model;

import com.project.troyoffice.dto.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "job_positions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"client_id", "code"})
})
@Audited
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPosition extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Client client;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(length = 50)
    private String level;

    @Column(name = "internal_grade_code", length = 50)
    private String internalGradeCode;

    @Column(name = "billing_rate_min")
    private BigDecimal billingRateMin;

    @Column(name = "billing_rate_max")
    private BigDecimal billingRateMax;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = true;

}
