package com.project.troyoffice.model;

import com.project.troyoffice.dto.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.UUID;

@Entity
@Table(name = "clients")
@Audited
@Getter
@Setter
public class Client extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String contactPerson;

    private String contactPhone;

    @Column(nullable = false)
    private Boolean isInternal = false;

//    @Column(columnDefinition = "TEXT")
//    private String billingConfig;

    private Boolean active = true;
}
