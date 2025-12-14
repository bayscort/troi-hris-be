package com.project.troyoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class EmployeeResponseDTO {

    private UUID id;
    private String fullName;

    private String employeeNumber;

    private Boolean active;

}
