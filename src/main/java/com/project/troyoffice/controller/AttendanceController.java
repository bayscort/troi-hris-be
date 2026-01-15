package com.project.troyoffice.controller;

import com.project.troyoffice.dto.AttendanceDetailResponse;
import com.project.troyoffice.dto.AttendanceRequest;
import com.project.troyoffice.dto.AttendanceResponse;
import com.project.troyoffice.dto.AttendanceStatsResponse;
import com.project.troyoffice.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AttendanceResponse processAttendance(@ModelAttribute AttendanceRequest request) {
        return attendanceService.processAttendance(request);
    }

    @GetMapping("/today")
    public AttendanceResponse getTodayDetail() {
        return attendanceService.getTodayDetail();
    }

    @GetMapping("/list")
    public List<AttendanceResponse> getAttendanceList(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end)
    {
        return attendanceService.getAttendanceList(start, end);
    }

    @GetMapping("/list/all")
    public List<AttendanceResponse> getAttendanceListAll(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end)
    {
        return attendanceService.getAttendanceListAll(start, end);
    }

    @GetMapping("/details-list")
    public List<AttendanceDetailResponse> getAttendanceDetailList(
            @RequestParam String employeeNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end)
    {
        return attendanceService.getAttendanceDetailList(employeeNumber, start, end);
    }

    @GetMapping("/stats")
    public AttendanceStatsResponse getMonthlyStats(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month)
    {
        LocalDate today = LocalDate.now();
        int targetYear = year != null ? year : today.getYear();
        int targetMonth = month != null ? month : today.getMonthValue();

        return attendanceService.getMonthlyStats(targetYear, targetMonth);
    }

    @GetMapping("/reverse-geo")
    public String getReverGeo(
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon)
    {
        return attendanceService.reverseGeocode(lat, lon);
    }
}
