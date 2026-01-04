package com.project.troyoffice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RosterDto {

    // Wrapper Response
    @Data
    @Builder
    public static class Response {
        private UUID clientSiteId;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<EmployeeRow> rows;
    }

    // Baris per Karyawan
    @Data
    @Builder
    public static class EmployeeRow {
        private UUID employeeId;
        private String name;
        private String nik;
        private String jobPosition;

        // Key: "2026-01-01" (String format YYYY-MM-DD)
        // Value: Detail Shift
        private Map<String, Cell> schedules;
    }

    // Kotak Shift (Cell)
    @Data
    @Builder
    public static class Cell {
        private UUID scheduleId; // Penting untuk edit/swap nanti

        private String shiftName;    // "Pagi"
        private String shiftCode;    // "S1"
        private String color;        // "#FFC107"

        private LocalTime startTime;
        private LocalTime endTime;

        private boolean isOff;       // Libur?
        private boolean isCrossDay;  // Shift malam?

        private String status;       // "PRESENT", "LATE", "FUTURE" (Optional)
    }
}
