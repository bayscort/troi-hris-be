package com.project.troyoffice.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class ShiftMasterDTO {

    private ClientDTO client;

    private String code;

    private String name;

    private LocalTime startTime;

    private LocalTime endTime;

    private boolean isCrossDay;

    private boolean isDayOff;

    private Integer lateToleranceMinutes = 0;

    private Integer clockInWindowMinutes = 60;

}
