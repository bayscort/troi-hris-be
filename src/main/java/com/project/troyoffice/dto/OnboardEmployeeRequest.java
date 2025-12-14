package com.project.troyoffice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class OnboardEmployeeRequest {
    @NotBlank
    private String fullName;
    @NotBlank
    private String employeeNumber;
    @NotBlank
    private String identityNumber;
    private String email;
    private String phoneNumber;
    private String address;

    // user
    private String username;
    private String password;
    private UUID roleId;
}
