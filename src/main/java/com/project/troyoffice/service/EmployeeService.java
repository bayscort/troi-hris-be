package com.project.troyoffice.service;

import com.project.troyoffice.dto.*;
import com.project.troyoffice.enums.EducationLevel;
import com.project.troyoffice.mapper.EmployeeMapper;
import com.project.troyoffice.model.*;
import com.project.troyoffice.repository.EmployeeRepository;
import com.project.troyoffice.repository.JobReferenceRepository;
import com.project.troyoffice.repository.PlacementRepository;
import com.project.troyoffice.specification.EmployeeSpecification;
import com.project.troyoffice.util.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final PlacementRepository placementRepository;

    private final EmployeeMapper employeeMapper;

    private final UserService userService;

    private final JobReferenceRepository jobReferenceRepository;

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> findAll() {
        final List<Employee> employeeList = employeeRepository.findByActiveTrue(Sort.by("id"));
        return employeeList.stream()
                .map(employeeMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO get(UUID id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toDTO)
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(EmployeeRequestDTO dto) {

        Employee employee = employeeMapper.toEntity(dto);
        employee.setActive(true);

        // =========================
        // EDUCATIONS
        // =========================
        if (dto.getEducations() != null) {
            List<EmployeeEducation> educations = dto.getEducations()
                    .stream()
                    .map(e -> {
                        EmployeeEducation edu = new EmployeeEducation();
                        edu.setSchoolName(e.getSchoolName());
                        edu.setLevel(e.getLevel());
                        edu.setMajor(e.getMajor());
                        edu.setStartYear(e.getStartYear());
                        edu.setEndYear(e.getEndYear());
                        edu.setEmployee(employee); // 🔥 IMPORTANT
                        return edu;
                    })
                    .toList();

            employee.setEducations(educations);
        }

        // =========================
        // JOB REFERENCES
        // =========================
        if (dto.getJobReferences() != null) {
            List<EmployeeJobReference> refs = dto.getJobReferences()
                    .stream()
                    .map(r -> {
                        EmployeeJobReference ref = new EmployeeJobReference();
                        ref.setEmployee(employee);

                        JobReference jobRef =
                                jobReferenceRepository.getReferenceById(r.getJobReferenceId());
                        ref.setJobReference(jobRef);

                        ref.setSkillLevel(r.getSkillLevel());
                        ref.setExperienceYears(r.getExperienceYears());
                        return ref;
                    })
                    .toList();

            employee.setJobReferences(refs);
        }

        return employeeRepository.save(employee).getId();
    }

    public UUID update(EmployeeRequestDTO dto) {

        Employee employee = employeeRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        // =========================
        // UPDATE BASIC FIELD
        // =========================
        employeeMapper.updateEntity(dto, employee);

        // =========================
        // UPDATE EDUCATIONS
        // =========================
        if (dto.getEducations() != null) {

            // Map existing by ID
            Map<UUID, EmployeeEducation> existingMap =
                    employee.getEducations().stream()
                            .filter(e -> e.getId() != null)
                            .collect(Collectors.toMap(EmployeeEducation::getId, Function.identity()));

            List<EmployeeEducation> updatedList = new ArrayList<>();

            for (EmployeeEducationDTO e : dto.getEducations()) {

                EmployeeEducation edu;

                if (e.getId() != null && existingMap.containsKey(e.getId())) {
                    // UPDATE EXISTING
                    edu = existingMap.get(e.getId());
                } else {
                    // CREATE NEW
                    edu = new EmployeeEducation();
                    edu.setEmployee(employee);
                }

                edu.setSchoolName(e.getSchoolName());
                edu.setLevel(e.getLevel());
                edu.setMajor(e.getMajor());
                edu.setStartYear(e.getStartYear());
                edu.setEndYear(e.getEndYear());

                updatedList.add(edu);
            }

            employee.getEducations().clear();
            employee.getEducations().addAll(updatedList);
        }

        // =========================
        // UPDATE JOB REFERENCES
        // =========================
        if (dto.getJobReferences() != null) {

            Map<UUID, EmployeeJobReference> existingMap =
                    employee.getJobReferences().stream()
                            .filter(j -> j.getId() != null)
                            .collect(Collectors.toMap(EmployeeJobReference::getId, Function.identity()));

            List<EmployeeJobReference> updatedList = new ArrayList<>();

            for (EmployeeJobReferenceDTO r : dto.getJobReferences()) {

                EmployeeJobReference ref;

                if (r.getId() != null && existingMap.containsKey(r.getId())) {
                    ref = existingMap.get(r.getId());
                } else {
                    ref = new EmployeeJobReference();
                    ref.setEmployee(employee);

                    JobReference jobReference =
                            jobReferenceRepository.getReferenceById(r.getJobReferenceId());
                    ref.setJobReference(jobReference);
                }

                ref.setSkillLevel(r.getSkillLevel());
                ref.setExperienceYears(r.getExperienceYears());
                ref.setPrimaryReference(Boolean.TRUE.equals(r.getPrimaryReference()));

                updatedList.add(ref);
            }

            employee.getJobReferences().clear();
            employee.getJobReferences().addAll(updatedList);
        }

        return employee.getId();
    }

    public void delete(UUID id) {
        Employee entity = employeeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        entity.setActive(false);
        employeeRepository.save(entity);
    }

    // 1. ONBOARDING
    public Employee onboardEmployee(OnboardEmployeeRequest req) {
        if (employeeRepository.existsByEmployeeNumber(req.getEmployeeNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "NIP already exists");
        }

        Employee employee = employeeMapper.toEntity(req);
        employee.setActive(true);

        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUsername(req.getUsername());
        userRequestDTO.setPassword(req.getPassword());
        userRequestDTO.setRoleId(req.getRoleId());
        userService.create(userRequestDTO);

        return employeeRepository.save(employee);
    }

    public void offboardEmployee(UUID id, OffboardRequest req) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // A. Terminate Active Placement (Jika ada)
        placementRepository.findByEmployeeIdAndIsActiveTrue(id).ifPresent(p -> {
            p.setIsActive(false);
            p.setEndDate(req.getEffectiveDate());
//            p.setTerminationReason("OFFBOARDING: " + req.getReason());
            placementRepository.save(p);
        });

        // B. Matikan Employee
        employee.setActive(false);
        // employee.setResignDate(...) // Jika ada field ini

        // C. Matikan User Login (Jika ada)
        if (employee.getUser() != null) {
            employee.getUser().setActive(false);
        }

        employeeRepository.save(employee);
    }

    // 3. GET ACTIVE (DEPLOYED)
    public List<EmployeeListResponse> getDeployedEmployees() {
        return employeeRepository.findDeployedEmployees()
                .stream()
                .map(e -> mapToResponse(e, "DEPLOYED")).toList();
    }

    // 4. GET TALENT POOL (BENCH)
    public List<EmployeeListResponse> getBenchEmployees() {
        log.info("employee list bench: {}",  employeeRepository.findBenchEmployees());
        return employeeRepository.findBenchEmployees()
                .stream()
                .map(e -> mapToResponse(e, "BENCH")).toList();
    }

    private EmployeeListResponse mapToResponse(Employee e, String status) {
        EmployeeListResponse res = new EmployeeListResponse();
        BeanUtils.copyProperties(e, res);
        res.setStatus(status);

        if ("DEPLOYED".equals(status)) {
            e.getPlacements().stream()
                    .filter(Placement::getIsActive)
                    .findFirst()
                    .ifPresent(p -> {
                        res.setCurrentClient(p.getClient().getName());
                        res.setJobTitle(p.getJobTitle());
                    });
        }
        return res;
    }

    public Page<EmployeeResponseDTO> search(
            List<UUID> jobReferenceIds,
            EducationLevel educationMin,
            EducationLevel educationMax,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("fullName").ascending()
        );

        Specification<Employee> spec = Specification
                .where(EmployeeSpecification.hasAnyJobReference(jobReferenceIds))
                .and(EmployeeSpecification.educationLevelBetween(
                        educationMin, educationMax
                ));

        return employeeRepository.findAll(
                (root, query, cb) -> {
                    query.distinct(true);
                    return spec.toPredicate(root, query, cb);
                },
                pageable
        ).map(employeeMapper::toDTO);
    }



}
