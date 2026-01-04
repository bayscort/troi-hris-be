package com.project.troyoffice.repository;

import com.project.troyoffice.model.EmployeeSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeScheduleRepository extends JpaRepository<EmployeeSchedule, UUID> {
    Optional<EmployeeSchedule> findByEmployeeIdAndDate(UUID employeeId, LocalDate date);

    @Query("SELECT s FROM EmployeeSchedule s " +
            "JOIN FETCH s.shiftMaster sm " +  // Optimasi: Ambil detail shift master sekaligus
            "WHERE s.employeeId IN :employeeIds " +
            "AND s.date BETWEEN :startDate AND :endDate")
    List<EmployeeSchedule> findByEmployeeIdsAndDateRange(
            @Param("employeeIds") List<UUID> employeeIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
