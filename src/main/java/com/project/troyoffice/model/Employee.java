package com.project.troyoffice.model;

import com.project.troyoffice.dto.base.AuditableEntity;
import com.project.troyoffice.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
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

    private String placeOfBirth;
    private LocalDate dateOfBirth;

    private String province;
    private String city;
    private String district;
    private String fullAddress;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String religion;
    private String bloodType;

    private Integer heightCm;
    private Integer weightKg;

//    private String parentName;
    private Integer familyMemberCount;

    private String emergencyContactName;
    private String emergencyContactPhone;

    private Boolean active;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Placement> placements;

    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<EmployeeJobReference> jobReferences;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeEducation> educations;

}
