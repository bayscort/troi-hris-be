package com.project.troyoffice.service;

import com.project.troyoffice.dto.AssignShiftPatternRequest;
import com.project.troyoffice.dto.BulkAssignShiftPatternRequest;
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
import java.util.*;
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

    public void bulkAssignPattern(BulkAssignShiftPatternRequest req) {
        ShiftPattern pattern = shiftPatternRepository.findById(req.getShiftPatternId())
                .orElseThrow(() -> new RuntimeException("Pattern not found"));

        for (UUID employeeId : req.getEmployeeIds()) {

            // 1. STEP PENTING: Tutup Assignment Lama (Jika ada)
            // Agar tidak ada 2 assignment aktif (double active)
            terminateActiveAssignment(employeeId, req.getEffectiveDate());

            // 2. Buat Assignment Baru
            EmployeeShiftAssignment assignment = new EmployeeShiftAssignment();
            assignment.setEmployeeId(employeeId);
            assignment.setShiftPattern(pattern);
            assignment.setEffectiveDate(req.getEffectiveDate()); // Mulai berlaku

            EmployeeShiftAssignment savedAssignment = assignmentRepo.save(assignment);

            // 3. Trigger Generator
            // Generate ulang jadwal dari tanggal efektif sampai 1 bulan ke depan
            LocalDate generateUntil = req.getEffectiveDate().plusMonths(1);
            generateForSingleEmployee(savedAssignment, req.getEffectiveDate(), generateUntil);
        }
    }

    /**
     * Helper: Menutup assignment lama H-1 sebelum assignment baru berlaku
     */
    private void terminateActiveAssignment(UUID employeeId, LocalDate newEffectiveDate) {
        Optional<EmployeeShiftAssignment> activeAssign = assignmentRepo.findByEmployeeIdAndEndDateIsNull(employeeId);

        if (activeAssign.isPresent()) {
            EmployeeShiftAssignment existing = activeAssign.get();

            // Set End Date ke HARI SEBELUM effective date baru
            // Contoh: Pola baru mulai 5 Jan. Pola lama stop di 4 Jan.
            LocalDate yesterday = newEffectiveDate.minusDays(1);

            // Validasi kecil: Jangan sampai end date < start date (kalau admin salah input tanggal mundur)
            if (yesterday.isBefore(existing.getEffectiveDate())) {
                // Skenario edge case: Admin menimpa assignment yang baru saja dibuat hari ini
                // Hapus saja assignment lama agar tidak error active date range invalid
                assignmentRepo.delete(existing);
            } else {
                existing.setEndDate(yesterday);
                assignmentRepo.save(existing);
            }
        }
    }

    /**
     * LOGIC GENERATOR (Diperbarui untuk Handle UPDATE & LOCKED)
     */
    @Transactional
    public void generateForSingleEmployee(EmployeeShiftAssignment assignment, LocalDate start, LocalDate end) {
        ShiftPattern pattern = assignment.getShiftPattern();
        if (pattern == null) return;

        // Map Sequence untuk akses O(1)
        Map<Integer, ShiftMaster> patternMap = pattern.getItems().stream()
                .collect(Collectors.toMap(ShiftPatternItem::getDaySequence, ShiftPatternItem::getShiftMaster));

        List<EmployeeSchedule> schedulesToSave = new ArrayList<>();

        LocalDate currentDate = start;
        while (!currentDate.isAfter(end)) {

            // A. Cari Jadwal Eksisting
            Optional<EmployeeSchedule> existingOpt = scheduleRepo.findByEmployeeIdAndDate(assignment.getEmployeeId(), currentDate);

            // B. Hitung Shift apa yang seharusnya dipakai hari ini
            long daysDiff = ChronoUnit.DAYS.between(assignment.getEffectiveDate(), currentDate);

            // Hanya proses jika tanggal >= effective date
            if (daysDiff >= 0) {
                int sequence = (int) (daysDiff % pattern.getCycleDays()) + 1;
                ShiftMaster targetShift = patternMap.get(sequence);

                if (targetShift != null) {
                    if (existingOpt.isPresent()) {
                        // --- SKENARIO UPDATE ---
                        EmployeeSchedule existingSchedule = existingOpt.get();

                        // CEK KUNCI: Jika LOCKED, skip (jangan diubah)
                        if (existingSchedule.isLocked()) {
                            log.info("Skipping schedule update for {} on {} because it is LOCKED", assignment.getEmployeeId(), currentDate);
                        } else {
                            // Jika TIDAK LOCKED, timpa datanya (Update)
                            updateScheduleEntity(existingSchedule, currentDate, targetShift);
                            schedulesToSave.add(existingSchedule);
                        }
                    } else {
                        // --- SKENARIO CREATE BARU ---
                        schedulesToSave.add(createScheduleEntity(assignment.getEmployeeId(), currentDate, targetShift));
                    }
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        if (!schedulesToSave.isEmpty()) {
            scheduleRepo.saveAll(schedulesToSave);
        }
    }

    // Helper: Membuat Object Baru
    private EmployeeSchedule createScheduleEntity(UUID employeeId, LocalDate date, ShiftMaster master) {
        EmployeeSchedule schedule = new EmployeeSchedule();
        schedule.setEmployeeId(employeeId);
        // Panggil helper update untuk set field-nya (biar kodingan gak duplikat)
        updateScheduleEntity(schedule, date, master);

        // Default value khusus object baru
        schedule.setLocked(false);
        return schedule;
    }

    // Helper: Update Object yang sudah ada (By Reference)
    private void updateScheduleEntity(EmployeeSchedule schedule, LocalDate date, ShiftMaster master) {
        // Kalkulasi Waktu
        LocalDateTime actualStart = LocalDateTime.of(date, master.getStartTime());
        LocalDateTime actualEnd = LocalDateTime.of(date, master.getEndTime());
        if (master.isCrossDay()) {
            actualEnd = actualEnd.plusDays(1);
        }

        // Set Values
        schedule.setDate(date);
        schedule.setShiftMaster(master);
        schedule.setActualStartTime(actualStart);
        schedule.setActualEndTime(actualEnd);
        schedule.setHoliday(master.isDayOff());
    }
}
