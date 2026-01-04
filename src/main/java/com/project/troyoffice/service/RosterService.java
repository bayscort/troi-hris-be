package com.project.troyoffice.service;

import com.project.troyoffice.dto.RosterDto;
import com.project.troyoffice.model.Employee;
import com.project.troyoffice.model.EmployeeSchedule;
import com.project.troyoffice.model.Placement;
import com.project.troyoffice.repository.EmployeeScheduleRepository;
import com.project.troyoffice.repository.PlacementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RosterService {

    private final PlacementRepository placementRepo;
    private final EmployeeScheduleRepository scheduleRepo;

    @Transactional(readOnly = true)
    public RosterDto.Response getRosterMatrix(UUID siteId, UUID jobPositionId, LocalDate start, LocalDate end) {

        // 1. Ambil Karyawan (Rows)
        List<Placement> placements = placementRepo.findEmployeesForRoster(siteId, jobPositionId, start, end);

        if (placements.isEmpty()) {
            return RosterDto.Response.builder()
                    .clientSiteId(siteId).startDate(start).endDate(end)
                    .rows(Collections.emptyList())
                    .build();
        }

        // 2. Extract Employee IDs untuk query jadwal
        List<UUID> employeeIds = placements.stream()
                .map(Placement::getEmployee)
                .map(Employee::getId)
                .collect(Collectors.toList());

        // 3. Ambil Semua Jadwal (Cells) - HANYA 1 QUERY
        List<EmployeeSchedule> allSchedules = scheduleRepo.findByEmployeeIdsAndDateRange(employeeIds, start, end);

        // 4. Grouping Jadwal per Employee di Memori
        // Map<EmployeeUUID, List<Schedule>>
        Map<UUID, List<EmployeeSchedule>> scheduleMap = allSchedules.stream()
                .collect(Collectors.groupingBy(EmployeeSchedule::getEmployeeId));

        // 5. Build DTO Response
        List<RosterDto.EmployeeRow> rows = placements.stream().map(p -> {

            // Ambil jadwal milik karyawan ini dari Map
            List<EmployeeSchedule> employeeSchedules = scheduleMap.getOrDefault(p.getEmployee().getId(), Collections.emptyList());

            // Convert List Schedule menjadi Map<DateString, Cell>
            Map<String, RosterDto.Cell> cellMap = new HashMap<>();

            for (EmployeeSchedule s : employeeSchedules) {
                cellMap.put(s.getDate().toString(), buildCell(s));
            }

            return RosterDto.EmployeeRow.builder()
                    .employeeId(p.getEmployee().getId())
                    .name(p.getEmployee().getFullName()) // Asumsi ada method getFullName
                    .nik(p.getEmployee().getEmployeeNumber())
                    .jobPosition(p.getJobPosition().getTitle())
                    .schedules(cellMap)
                    .build();

        }).collect(Collectors.toList());

        // Sort karyawan berdasarkan Nama (Opsional)
        rows.sort(Comparator.comparing(RosterDto.EmployeeRow::getName));

        return RosterDto.Response.builder()
                .clientSiteId(siteId)
                .startDate(start)
                .endDate(end)
                .rows(rows)
                .build();
    }

    private RosterDto.Cell buildCell(EmployeeSchedule s) {
        // Logic status sederhana (Bisa dikembangkan nanti cek log absensi)
        String status = "FUTURE";
        if (s.getDate().isBefore(LocalDate.now())) {
            status = "PAST"; // Nanti bisa diganti logic cek AttendanceLog
        }

        return RosterDto.Cell.builder()
                .scheduleId(s.getId())
                .shiftName(s.getShiftMaster().getName())
                .shiftCode(s.getShiftMaster().getCode())
                .color(s.getShiftMaster().getColor()) // Pastikan entity ShiftMaster punya field color
                .startTime(s.getShiftMaster().getStartTime())
                .endTime(s.getShiftMaster().getEndTime())
                .isOff(s.getShiftMaster().isDayOff())
                .isCrossDay(s.getShiftMaster().isCrossDay())
                .status(status)
                .build();
    }
}