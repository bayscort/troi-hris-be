package com.project.troyoffice.model;

import com.project.troyoffice.util.UserContextUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "employee_shift_assignments")
@Getter
@Setter
public class EmployeeShiftAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    // Opsi 1: Menggunakan Pattern (Rotasi)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_pattern_id")
    private ShiftPattern shiftPattern;

    // Opsi 2: Fixed Shift (Senin-Minggu sama terus, jarang dipakai tapi perlu ada)
    // Jika pattern null, sistem cek ini.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_shift_master_id")
    private ShiftMaster fixedShiftMaster;

    // Kapan pola ini mulai berlaku? (Penting untuk kalkulasi modulus hari)
    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    // Sampai kapan? (Null = Forever)
    @Column(name = "end_date")
    private LocalDate endDate;

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
