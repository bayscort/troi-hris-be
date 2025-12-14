package com.project.troyoffice.dto;

import com.project.troyoffice.enums.VerificationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AttendanceResponse {

    private UUID id;
    private LocalDate date;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String employeeName;
    private String checkInLatitude;
    private String checkOutLatitude;
    private String checkInLongitude;
    private String checkOutLongitude;
    private VerificationStatus status;
    private String location;
    private Double totalHours;

}
