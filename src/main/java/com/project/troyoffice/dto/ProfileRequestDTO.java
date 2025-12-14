package com.project.troyoffice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileRequestDTO {

    @NotNull
    private UserRequestDTO user;

    @NotNull
    private EmployeeRequestDTO employee;

}
