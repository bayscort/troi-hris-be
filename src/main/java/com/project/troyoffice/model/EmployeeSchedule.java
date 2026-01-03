package com.project.troyoffice.model;

import com.project.troyoffice.util.UserContextUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "employee_schedules", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_id", "date"})
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    // Tanggal Shift (Bukan jam masuk, tapi tanggal kalender)
    @Column(nullable = false)
    private LocalDate date;

    // Final Shift yang berlaku hari ini (Hasil generate dari Pattern ATAU Manual Swap)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shift_master_id", nullable = false)
    private ShiftMaster shiftMaster;

    // --- OVERRIDE CAPABILITY ---
    // Kadang SPV ingin ubah jam spesifik untuk 1 orang tanpa ubah Master Shift
    // Field ini diisi oleh Scheduler dari ShiftMaster, tapi bisa diedit manual.

    @Column(name = "actual_start_time")
    private LocalDateTime actualStartTime; // Gabungan Date + StartTime

    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;   // Gabungan Date + EndTime (handle cross day)

    // Flag: Apakah ini hari libur? (Derived dari shiftMaster.isDayOff)
    @Column(name = "is_holiday")
    private boolean isHoliday = false;

    // Flag: Jika TRUE, Scheduler tidak boleh menimpa data ini lagi
    // (Misal: Sudah ditukar manual oleh SPV)
    @Column(name = "is_locked")
    private boolean isLocked = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        createdBy = UserContextUtil.getCurrentUsername();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        updatedBy = UserContextUtil.getCurrentUsername();
    }
}
