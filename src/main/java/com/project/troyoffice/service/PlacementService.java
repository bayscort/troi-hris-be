package com.project.troyoffice.service;

import com.project.troyoffice.dto.DeployEmployeeRequest;
import com.project.troyoffice.dto.EmployeeAssignmentDto;
import com.project.troyoffice.model.*;
import com.project.troyoffice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PlacementService {

    private final PlacementRepository placementRepository;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final ClientSiteRepository clientSiteRepository;
    private final JobPositionRepository jobPositionRepository;
    private final EmployeeShiftAssignmentRepository assignmentRepository;

    // 5. DEPLOY EMPLOYEE
    public Placement deployEmployee(DeployEmployeeRequest req) {
        Employee employee = employeeRepository.findById(req.getEmployeeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));

        if (!employee.getActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot deploy inactive employee");
        }

        // Validasi: Apakah karyawan ini sedang bekerja di tempat lain?
        if (placementRepository.findByEmployeeIdAndIsActiveTrue(req.getEmployeeId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Employee is currently deployed. Terminate current contract first.");
        }

        Client client = clientRepository.findById(req.getClientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        ClientSite site = null;
        if (req.getClientSiteId() != null) {
            site = clientSiteRepository.findById(req.getClientSiteId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Site not found"));
        }

        JobPosition jobPosition = null;
        if (req.getJobPositionId() != null) {
            jobPosition = jobPositionRepository.findById(req.getJobPositionId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job position not found"));
        }

        Placement placement = new Placement();
        placement.setEmployee(employee);
        placement.setClient(client);
        placement.setClientSite(site);
        placement.setJobTitle(req.getJobTitle());
        placement.setJobPosition(jobPosition);
        placement.setEmployeeIdAtClient(req.getEmployeeIdAtClient());
        placement.setEmploymentType(req.getEmploymentType());
        placement.setStartDate(req.getStartDate());
        placement.setEndDate(req.getEndDate());
        placement.setBasicSalary(req.getBasicSalary());
        placement.setIsActive(true);

        return placementRepository.save(placement);
    }

    // 6. ACTIVE PLACEMENTS LIST
    public Page<Placement> getActivePlacements(Pageable pageable) {
        return placementRepository.findByIsActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    public List<EmployeeAssignmentDto> getActiveEmployeesForAssignment(UUID siteId, UUID jobPositionId) {

        // 1. Ambil semua Placement Aktif di Site tersebut
        List<Placement> placements = placementRepository.findActivePlacementsBySite(siteId, jobPositionId);

        if (placements.isEmpty()) {
            return Collections.emptyList();
        }

        List<UUID> employeeIds = placements.stream()
                .map(Placement::getEmployee)
                .map(Employee::getId)
                .toList();

        List<EmployeeShiftAssignment> assignments = assignmentRepository.findCurrentAssignments(employeeIds, LocalDate.now());

        // 4. Map Assignment ke dalam Map agar pencarian O(1)
        // Map<EmployeeID, Assignment>
        Map<UUID, EmployeeShiftAssignment> assignmentMap = assignments.stream()
                .collect(Collectors.toMap(
                        EmployeeShiftAssignment::getEmployeeId,
                        a -> a,
                        (existing, replacement) -> existing // Jika ada duplikat (jarang), ambil yg pertama
                ));

        // 5. Gabungkan Data (Placement + Pattern Info)
        return placements.stream().map(p -> {
            EmployeeShiftAssignment activeAssignment = assignmentMap.get(p.getEmployee().getId());

            String patternName = "-";
            UUID patternId = null;

            if (activeAssignment != null && activeAssignment.getShiftPattern() != null) {
                patternName = activeAssignment.getShiftPattern().getName();
                patternId = activeAssignment.getShiftPattern().getId();
            }

            return EmployeeAssignmentDto.builder()
                    .employeeId(p.getEmployee().getId())
                    .name(p.getEmployee().getFullName())
                    .nik(p.getEmployee().getEmployeeNumber())
                    .placementId(p.getId())
                    .jobPositionName(p.getJobPosition().getTitle())
                    .currentPatternId(patternId)
                    .currentPatternName(patternName)
                    .build();
        }).collect(Collectors.toList());
    }
}
