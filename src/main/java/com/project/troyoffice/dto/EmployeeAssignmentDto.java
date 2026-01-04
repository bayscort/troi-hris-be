package com.project.troyoffice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class EmployeeAssignmentDto {
    private UUID employeeId;
    private String name;
    private String nik;

    private UUID placementId;
    private String jobPositionName;

    private UUID currentPatternId;
    private String currentPatternName; // e.g., "Security 3 Shift" atau "-"
}