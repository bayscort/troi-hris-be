package com.project.troyoffice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreatePlacementRequest {
    @NotNull
    private UUID employeeId;
    @NotNull
    private UUID clientSiteId;
    @NotNull
    private UUID jobPositionId;
    @NotNull
    private LocalDate startDate;
    private String contractNumber;
}
