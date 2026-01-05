package com.project.troyoffice.dto;

import com.project.troyoffice.enums.EmploymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class DeployEmployeeRequest {
    @NotNull
    private UUID employeeId;

    @NotNull
    private UUID clientId;

    private UUID clientSiteId;

    @NotNull
    private UUID jobPositionId;

    private String jobTitle;

    private String employeeIdAtClient;

    @NotNull
    private EmploymentType employmentType;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    private BigDecimal basicSalary;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private UUID roleId;
}

