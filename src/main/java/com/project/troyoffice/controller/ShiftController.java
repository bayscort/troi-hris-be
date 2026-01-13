package com.project.troyoffice.controller;

import com.project.troyoffice.dto.BulkAssignShiftPatternRequest;
import com.project.troyoffice.dto.ManualScheduleDto;
import com.project.troyoffice.model.Employee;
import com.project.troyoffice.repository.EmployeeRepository;
import com.project.troyoffice.service.ShiftService;
import com.project.troyoffice.util.ExcelHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/shifts", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class ShiftController {

    private final ShiftService shiftService;
    private final EmployeeRepository employeeRepository;

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

    @PostMapping("/bulk-assign-pattern")
    public ResponseEntity<?> bulkAssignPattern(@RequestBody @Valid BulkAssignShiftPatternRequest req) {
        shiftService.bulkAssignPattern(req);
        return ResponseEntity.ok("Pattern assigned and schedule generated successfully");
    }

    @PostMapping(value = "/upload-manual", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {

        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                // 1. Parse Excel jadi DTO
                List<ManualScheduleDto> dtos = ExcelHelper.excelToSchedulesMatrix(file.getInputStream());
                // 2. Pre-processing: Convert Employee Code (NIK) ke UUID
                // Agar tidak query DB satu per satu di dalam loop service
                enrichEmployeeIds(dtos);

                shiftService.importManualSchedules(dtos);

                return ResponseEntity.ok("Uploaded successfully: " + dtos.size() + " records processed.");

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Gagal upload: " + e.getMessage());
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload an excel file!");
    }

    // Helper kecil untuk mencari UUID berdasarkan NIK secara batch
    private void enrichEmployeeIds(List<ManualScheduleDto> dtos) {

        for (ManualScheduleDto dto : dtos) {
            log.info("nik: {}", dto.getEmployeeCode());
        }

        // Ambil list NIK dari Excel
        List<String> codes = dtos.stream().map(ManualScheduleDto::getEmployeeCode).toList();
        log.info("codes: {}", codes);

        // Query Batch ke DB: Select * from employee where nik IN (...)
        Map<String, UUID> employeeMap = employeeRepository.findByEmployeeNumberIn(codes)
                .stream()
                .collect(Collectors.toMap(Employee::getEmployeeNumber, Employee::getId));

        log.info("employeeMap: {}", employeeMap);

        // Map UUID kembali ke DTO
        for (ManualScheduleDto dto : dtos) {
            UUID id = employeeMap.get(dto.getEmployeeCode());
            if (id == null) {
                throw new RuntimeException("Employee dengan NIK " + dto.getEmployeeCode() + " tidak ditemukan.");
            }
            dto.setEmployeeId(id);
        }
    }

}
