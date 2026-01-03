package com.project.troyoffice.repository;

import com.project.troyoffice.model.EmployeeShiftAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeShiftAssignmentRepository extends JpaRepository<EmployeeShiftAssignment, UUID> {
    // Cari assignment yang aktif pada tanggal tertentu
    @Query("SELECT a FROM EmployeeShiftAssignment a WHERE a.effectiveDate <= :targetDate AND (a.endDate IS NULL OR a.endDate >= :targetDate)")
    List<EmployeeShiftAssignment> findAllActiveAssignments(LocalDate targetDate);

    Optional<EmployeeShiftAssignment> findByEmployeeIdAndEndDateIsNull(UUID employeeId);
}
