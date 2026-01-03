package com.project.troyoffice.dto;

import com.project.troyoffice.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class EmployeeRequestDTO {

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

    private List<EmployeeJobReferenceDTO> jobReferences;

    private List<EmployeeEducationDTO> educations;

}
