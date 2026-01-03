package com.project.troyoffice.model;

import com.project.troyoffice.dto.base.AuditableEntity;
import com.project.troyoffice.enums.SkillLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "employee_job_reference",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"employee_id", "job_reference_id"})
        }
)
@Getter
@Setter
@Audited
public class EmployeeJobReference extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_reference_id", nullable = false)
    private JobReference jobReference;

    // SKILL & EXPERIENCE
    @Enumerated(EnumType.STRING)
    private SkillLevel skillLevel;

    private Integer experienceYears;

    // CERTIFICATION
    private String certificationName;
    private String certificationNumber;

    private LocalDate certificationIssuedDate;
    private LocalDate certificationExpiryDate;

    private Boolean certified = false;

    // STATUS
    private Boolean primaryReference = false;
    private Boolean active = true;
}

