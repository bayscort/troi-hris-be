package com.project.troyoffice.dto;

import com.project.troyoffice.enums.Gender;
import com.project.troyoffice.model.EmployeeEducation;
import com.project.troyoffice.model.EmployeeJobReference;
import com.project.troyoffice.model.Placement;
import com.project.troyoffice.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class EmployeeResponseDTO {

    private UUID id;

    private String fullName;

    private String employeeNumber;

    private String identityNumber;

    private String email;

    private String phoneNumber;

    private String placeOfBirth;
    private LocalDate dateOfBirth;

    private String province;
    private String city;
    private String district;
    private String fullAddress;

    private Gender gender;

    private String religion;
    private String bloodType;

    private Integer heightCm;
    private Integer weightKg;

    private Integer familyMemberCount;

    private String emergencyContactName;
    private String emergencyContactPhone;

    private Boolean active;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", unique = true)
//    private User user;

//    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
//    private List<Placement> placements;

    private List<EmployeeJobReferenceDTO> jobReferences;

    private List<EmployeeEducationDTO> educations;

}
