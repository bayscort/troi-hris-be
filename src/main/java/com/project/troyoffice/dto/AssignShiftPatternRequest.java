package com.project.troyoffice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AssignShiftPatternRequest {
    @NotNull
    private UUID employeeId;
    @NotNull
    private UUID shiftPatternId;
    @NotNull
    private LocalDate effectiveDate;
}
