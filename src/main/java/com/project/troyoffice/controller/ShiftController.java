package com.project.troyoffice.controller;

import com.project.troyoffice.dto.AssignShiftPatternRequest;
import com.project.troyoffice.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/shifts", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    // API 1: Assign Pattern ke Karyawan
    // POST /api/v1/shifts/assign
//    @PostMapping("/assign")
//    public ResponseEntity<String> assignShiftPattern(@RequestBody AssignShiftRequest request) {
//        // Logic simpan ke table employee_shift_assignments
//        // Lalu trigger generate schedule untuk karyawan ini saja
//        return ResponseEntity.ok("Pattern assigned successfully");
//    }

    // API 2: Manual Trigger Generate (Misal: Admin baru ubah pattern, ingin update segera)
    // POST /api/v1/shifts/generate?start=2024-01-01&end=2024-01-31
    @PostMapping("/generate")
    public ResponseEntity<String> manualGenerate(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        shiftService.generateSchedules(startDate, endDate);
        return ResponseEntity.ok("Schedule generation triggered successfully");
    }

    // API 3: Get Schedule Karyawan (Untuk Mobile Apps)
    // GET /api/v1/shifts/my-schedule?date=2024-01-01
    @GetMapping("/my-schedule")
    public ResponseEntity<Object> getMySchedule(
            @RequestParam("employeeId") UUID employeeId,
            @RequestParam("date") LocalDate date) {
        // Return JSON jadwal hari itu
        return ResponseEntity.ok().build();
    }

    @PostMapping("/assign-pattern")
    public ResponseEntity<?> assignPattern(@RequestBody @Valid AssignShiftPatternRequest req) {
        shiftService.assignPattern(req);
        return ResponseEntity.ok("Pattern assigned and schedule generated successfully");
    }

}
