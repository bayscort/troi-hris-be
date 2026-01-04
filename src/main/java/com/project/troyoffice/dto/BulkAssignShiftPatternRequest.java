package com.project.troyoffice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class BulkAssignShiftPatternRequest {
    @NotNull
    private List<UUID> employeeIds;
    @NotNull
    private UUID shiftPatternId;
    @NotNull
    private LocalDate effectiveDate;
}
