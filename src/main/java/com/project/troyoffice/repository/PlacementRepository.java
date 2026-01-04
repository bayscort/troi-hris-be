package com.project.troyoffice.repository;

import com.project.troyoffice.model.Employee;
import com.project.troyoffice.model.Placement;
import com.project.troyoffice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlacementRepository extends JpaRepository<Placement, UUID> {

    List<Placement> findByIsActiveTrue(Sort sort);

    Optional<Placement> findPlacementByEmployeeAndIsActiveTrue(Employee employee);

    Optional<Placement> findByEmployeeIdAndIsActiveTrue(UUID employeeId);

    Page<Placement> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT p FROM Placement p " +
            "JOIN FETCH p.employee e " +      // Fetch Employee Eagerly
            "JOIN FETCH p.jobPosition j " +   // Fetch Job Eagerly
            "WHERE p.clientSite.id = :siteId " +
            "AND (:jobPositionId IS NULL OR p.jobPosition.id = :jobPositionId) " +
            // Logic Active Placement:
            "AND p.startDate <= CURRENT_DATE " +
            "AND (p.endDate IS NULL OR p.endDate >= CURRENT_DATE)")
    List<Placement> findActivePlacementsBySite(
            @Param("siteId") UUID siteId,
            @Param("jobPositionId") UUID jobPositionId
    );

    @Query("SELECT p FROM Placement p " +
            "JOIN FETCH p.employee e " +      // Optimasi: Ambil data employee sekaligus
            "JOIN FETCH p.jobPosition j " +   // Optimasi: Ambil jabatan sekaligus
            "WHERE p.clientSite.id = :siteId " +
            "AND (:jobPositionId IS NULL OR p.jobPosition.id = :jobPositionId) " +
            "AND p.startDate <= :endDate " +
            "AND (p.endDate IS NULL OR p.endDate >= :startDate)")
    List<Placement> findEmployeesForRoster(
            @Param("siteId") UUID siteId,
            @Param("jobPositionId") UUID jobPositionId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
