package com.project.troyoffice.repository;

import com.project.troyoffice.model.EmployeeSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeScheduleRepository extends JpaRepository<EmployeeSchedule, UUID> {
    Optional<EmployeeSchedule> findByEmployeeIdAndDate(UUID employeeId, LocalDate date);
}
