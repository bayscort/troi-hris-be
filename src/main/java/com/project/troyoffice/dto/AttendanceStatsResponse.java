package com.project.troyoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceStatsResponse {

    private String employeeName;
    private String monthYear;
    private int totalWorkingDays;
    private int daysPresent;
    private int daysAbsent;
    private int daysLateCheckIn;
//    private long totalWorkingHours;
//    private double averageWorkingHours;

}
