package com.project.troyoffice.repository;

import com.project.troyoffice.model.Attendance;
import com.project.troyoffice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

    Optional<Attendance> findByEmployeeAndCheckInTimeAfter(Employee employee, LocalDateTime startOfDay);

    List<Attendance> findByEmployeeAndCheckInTimeBetweenOrderByCheckInTimeDesc(
            Employee employee, LocalDateTime startDate, LocalDateTime endDate);
}