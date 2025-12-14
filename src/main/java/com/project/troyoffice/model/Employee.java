package com.project.troyoffice.model;

import com.project.troyoffice.dto.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.envers.Audited;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "employee")
@Audited
@Getter
@Setter
public class Employee extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String employeeNumber;

    @Column(unique = true)
    private String identityNumber;

    private String email;

    private String phoneNumber;

    private Boolean active;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<Placement> placements;

}
