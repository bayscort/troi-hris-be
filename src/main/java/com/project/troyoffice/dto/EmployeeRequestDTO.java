package com.project.troyoffice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequestDTO {

    @NotBlank
    private String fullName;

    @NotBlank
    private String employeeNumber;

    private Boolean active = true;

}
