package com.project.troyoffice.service;

import com.project.troyoffice.dto.*;
import com.project.troyoffice.mapper.EmployeeMapper;
import com.project.troyoffice.mapper.RoleMapper;
import com.project.troyoffice.model.Employee;
import com.project.troyoffice.model.Placement;
import com.project.troyoffice.model.Role;
import com.project.troyoffice.repository.EmployeeRepository;
import com.project.troyoffice.repository.PlacementRepository;
import com.project.troyoffice.repository.RoleRepository;
import com.project.troyoffice.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final PlacementRepository placementRepository;

    private final EmployeeMapper employeeMapper;

    private final UserService userService;

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
        Employee entity = employeeMapper.toEntity(dto);
        entity.setActive(true);
        return employeeRepository.save(entity).getId();
    }

    public void update(UUID id, EmployeeRequestDTO dto) {
        Employee entity = employeeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        employeeMapper.toUpdate(entity, dto);
        employeeRepository.save(entity);
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



}
