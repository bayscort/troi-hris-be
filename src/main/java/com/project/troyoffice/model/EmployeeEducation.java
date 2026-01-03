package com.project.troyoffice.model;

import com.project.troyoffice.dto.base.AuditableEntity;
import com.project.troyoffice.enums.EducationLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.UUID;

@Entity
@Table(name = "employee_education")
@Getter
@Setter
@Audited
public class EmployeeEducation extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private String schoolName;

    @Enumerated(EnumType.STRING)
    private EducationLevel level;

    private String major;
    private Integer startYear;
    private Integer endYear;
}

