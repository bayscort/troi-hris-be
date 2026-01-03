package com.project.troyoffice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class CreateShiftMasterRequest {
    @NotNull
    private UUID clientId;
    @NotNull
    private String code;
    @NotNull
    private String name;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    private boolean isCrossDay;
    private boolean isDayOff;
    private Integer lateToleranceMinutes = 0;
    private Integer clockInWindowMinutes = 60;
}
