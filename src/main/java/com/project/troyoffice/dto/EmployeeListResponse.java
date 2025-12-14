package com.project.troyoffice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class EmployeeListResponse {
    private UUID id;
    private String fullName;
    private String employeeNumber;
    private String status;
    private String currentClient;
    private String jobTitle;
}
