package com.project.troyoffice.model;

import com.project.troyoffice.dto.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.UUID;

@Entity
@Table(name = "job_reference")
@Getter
@Setter
@Audited
public class JobReference extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code; // SECURITY, DRIVER, CLEANING

    @Column(nullable = false)
    private String name; // Security Guard, Driver, Cleaning Service

    private String description;

    private Boolean active = true;
}

