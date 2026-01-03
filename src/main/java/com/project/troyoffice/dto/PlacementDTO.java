package com.project.troyoffice.dto;

import com.project.troyoffice.enums.EmploymentType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class PlacementDTO {
    private UUID id;

    private ClientDTO client;

    private ClientSiteDTO clientSite;

    private JobPositionDTO jobPosition;

    private String jobTitle;

    private String employeeIdAtClient;

    private EmploymentType employmentType;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal basicSalary;

    private Boolean isActive = true;
}
