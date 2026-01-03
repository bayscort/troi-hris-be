package com.project.troyoffice.service;

import com.project.troyoffice.dto.AssignShiftPatternRequest;
import com.project.troyoffice.model.*;
import com.project.troyoffice.repository.EmployeeScheduleRepository;
import com.project.troyoffice.repository.EmployeeShiftAssignmentRepository;
import com.project.troyoffice.repository.ShiftPatternRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShiftService {

    private final EmployeeShiftAssignmentRepository assignmentRepo;
    private final EmployeeScheduleRepository scheduleRepo;

    private final ShiftPatternRepository shiftPatternRepository;

    /**
     * CORE LOGIC: Generate Jadwal Massal
     * Method ini akan dipanggil oleh Scheduler atau API Manual
     */
    @Transactional
    public void generateSchedules(LocalDate startDate, LocalDate endDate) {
        log.info("Starting schedule generation from {} to {}", startDate, endDate);

        // 1. Ambil semua assignment aktif
        // Note: Untuk production dgn ribuan data, sebaiknya gunakan Pagination/Batch processing
        List<EmployeeShiftAssignment> assignments = assignmentRepo.findAllActiveAssignments(startDate);

        for (EmployeeShiftAssignment assignment : assignments) {
            generateForSingleEmployee(assignment, startDate, endDate);
        }
    }

    private void generateForSingleEmployee(EmployeeShiftAssignment assignment, LocalDate start, LocalDate end) {
        // Pre-load items pattern biar ga query berulang dalam loop
        ShiftPattern pattern = assignment.getShiftPattern();
        if (pattern == null) return; // Skip jika fixed shift (logic beda dikit)

        // Map Sequence Hari -> Shift Master (Biar akses cepat O(1))
        Map<Integer, ShiftMaster> patternMap = pattern.getItems().stream()
                .collect(Collectors.toMap(ShiftPatternItem::getDaySequence, ShiftPatternItem::getShiftMaster));

        List<EmployeeSchedule> newSchedules = new ArrayList<>();

        // Loop setiap hari dalam range yang diminta
        LocalDate currentDate = start;
        while (!currentDate.isAfter(end)) {

            // 2. Cek apakah sudah ada jadwal di tanggal ini?
            // Jika sudah ada dan IS_LOCKED = true, jangan ditimpa (itu hasil tukar shift manual)
            boolean exists = scheduleRepo.findByEmployeeIdAndDate(assignment.getEmployeeId(), currentDate)
                    .map(EmployeeSchedule::isLocked)
                    .orElse(false);

            if (!exists) {
                // 3. Hitung Matematika Pola (Modulo Arithmetic)
                // Berapa hari selisih dari effective date?
                long daysDiff = ChronoUnit.DAYS.between(assignment.getEffectiveDate(), currentDate);

                if (daysDiff >= 0) {
                    // Rumus: (Selisih Hari % Total Siklus) + 1
                    // Contoh: Hari ke-0 = Sequence 1. Hari ke-1 = Sequence 2.
                    int sequence = (int) (daysDiff % pattern.getCycleDays()) + 1;

                    ShiftMaster shiftToBeApplied = patternMap.get(sequence);

                    if (shiftToBeApplied != null) {
                        newSchedules.add(createScheduleEntity(assignment.getEmployeeId(), currentDate, shiftToBeApplied));
                    }
                }
            }
            currentDate = currentDate.plusDays(1);
        }

        // 4. Batch Save
        if (!newSchedules.isEmpty()) {
            scheduleRepo.saveAll(newSchedules);
            log.info("Generated {} schedules for employee {}", newSchedules.size(), assignment.getEmployeeId());
        }
    }

    private EmployeeSchedule createScheduleEntity(UUID employeeId, LocalDate date, ShiftMaster master) {
        // Gabungkan Date + Time untuk jadi LocalDateTime
        LocalDateTime actualStart = LocalDateTime.of(date, master.getStartTime());
        LocalDateTime actualEnd = LocalDateTime.of(date, master.getEndTime());

        // Handle Cross Day (Shift Malam)
        // Jika 22:00 - 06:00, maka end time harus tanggal besoknya
        if (master.isCrossDay()) {
            actualEnd = actualEnd.plusDays(1);
        }

        return EmployeeSchedule.builder()
                .employeeId(employeeId)
                .date(date)
                .shiftMaster(master)
                .actualStartTime(actualStart)
                .actualEndTime(actualEnd)
                .isHoliday(master.isDayOff()) // Inherit dari master
                .isLocked(false) // Auto generate selalu false
                .build();
    }

    public void assignPattern(AssignShiftPatternRequest req) {
        ShiftPattern pattern = shiftPatternRepository.findById(req.getShiftPatternId())
                .orElseThrow(() -> new RuntimeException("Pattern not found"));

        // Save Assignment ke DB
        EmployeeShiftAssignment assignment = new EmployeeShiftAssignment();
        assignment.setEmployeeId(req.getEmployeeId());
        assignment.setShiftPattern(pattern);
        assignment.setEffectiveDate(req.getEffectiveDate());

        EmployeeShiftAssignment savedAssignment = assignmentRepo.save(assignment);

        // --- TRIGGER GENERATOR ---
        // Generate jadwal dari EffectiveDate sampai 1 bulan ke depan
        // Agar admin langsung bisa lihat jadwalnya detik itu juga.
        LocalDate generateUntil = req.getEffectiveDate().plusMonths(1);

        // Panggil logic looping matematika yang dibuat di chat sebelumnya
        // (Pastikan method generateForSingleEmployee diubah jadi public di ShiftService)
        generateForSingleEmployee(savedAssignment, req.getEffectiveDate(), generateUntil);
    }
}
