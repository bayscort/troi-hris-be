package com.project.troyoffice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ManualScheduleDto {
    private UUID employeeId;
    private String employeeCode; // Opsi jika Excel pakai NIK/Code
    private String employeeNumber; // Opsi jika Excel pakai NIK/Code
    private LocalDate date;
    private String shiftCode; // Kode shift master (misal: "S1", "OFF", "M")
}
