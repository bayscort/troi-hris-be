package com.project.troyoffice.dto;

import com.project.troyoffice.enums.EducationLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class EmployeeEducationDTO {
    private UUID id;
    private String schoolName;
    private EducationLevel level;
    private String major;
    private Integer startYear;
    private Integer endYear;
}
