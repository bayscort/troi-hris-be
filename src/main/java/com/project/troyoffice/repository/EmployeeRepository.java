package com.project.troyoffice.repository;

import com.project.troyoffice.model.Employee;
import com.project.troyoffice.model.Role;
import com.project.troyoffice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    List<Employee> findByActiveTrue(Sort sort);

    boolean existsByEmployeeNumber(String employeeNumber);

    Optional<Employee> findByUser(User user);

    Optional<Employee> findByFullName(String name);
    Optional<Employee> findByEmployeeNumber(String employeeNumber);

    @Query("SELECT DISTINCT e FROM Employee e " +
            "JOIN e.placements p " +
            "WHERE e.active = true AND p.isActive = true")
    List<Employee> findDeployedEmployees();

    @Query(value = "SELECT *\n" +
            "    FROM {h-schema}employee e\n" +
            "    WHERE e.active = TRUE\n" +
            "      AND NOT EXISTS (\n" +
            "          SELECT 1\n" +
            "          FROM {h-schema}placements p\n" +
            "          WHERE p.employee_id = e.id\n" +
            "            AND p.is_active = TRUE)", nativeQuery = true)
    List<Employee> findBenchEmployees();

    boolean existsByIdentityNumber(String identityNumber);

}
