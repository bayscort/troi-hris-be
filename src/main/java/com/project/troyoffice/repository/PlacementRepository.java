package com.project.troyoffice.repository;

import com.project.troyoffice.model.Employee;
import com.project.troyoffice.model.Placement;
import com.project.troyoffice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlacementRepository extends JpaRepository<Placement, UUID> {

    List<Placement> findByIsActiveTrue(Sort sort);

    Optional<Placement> findPlacementByEmployeeAndIsActiveTrue(Employee employee);

    Optional<Placement> findByEmployeeIdAndIsActiveTrue(UUID employeeId);

    Page<Placement> findByIsActiveTrue(Pageable pageable);

}
