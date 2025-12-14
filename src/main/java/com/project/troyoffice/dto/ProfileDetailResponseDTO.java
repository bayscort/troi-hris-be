package com.project.troyoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProfileDetailResponseDTO {

    private UserResponseDTO user;
    private EmployeeResponseDTO employee;


}
