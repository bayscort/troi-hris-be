package com.project.troyoffice.dto;

import com.project.troyoffice.enums.SkillLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class EmployeeJobReferenceDTO {

    private UUID id;

    private UUID jobReferenceId;

    private SkillLevel skillLevel;
    private Integer experienceYears;

    private String certificationName;
    private String certificationNumber;

    private LocalDate certificationIssuedDate;
    private LocalDate certificationExpiryDate;

    private Boolean certified;
    private Boolean primaryReference;

}
