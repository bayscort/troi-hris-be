package com.project.troyoffice.controller;

import com.project.troyoffice.dto.*;
import com.project.troyoffice.model.Employee;
import com.project.troyoffice.service.EmployeeService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/employees", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getById(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(employeeService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> create(@RequestBody EmployeeRequestDTO dto) {
        final UUID createdId = employeeService.create(dto);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> update(@PathVariable(name = "id") UUID id,
                                       @RequestBody EmployeeRequestDTO dto) {
        employeeService.update(dto);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // FEATURE: Talent Pool & Active Employee
    // GET /api/v1/employees?status=BENCH
    // GET /api/v1/employees?status=DEPLOYED
    @GetMapping("/list")
    public ResponseEntity<List<EmployeeListResponse>> getEmployees(
            @RequestParam(defaultValue = "DEPLOYED") String status) {

        if ("BENCH".equalsIgnoreCase(status)) {
            return ResponseEntity.ok(employeeService.getBenchEmployees());
        } else {
            return ResponseEntity.ok(employeeService.getDeployedEmployees());
        }
    }

    // FEATURE: Onboarding
    @PostMapping("/onboard")
    public ResponseEntity<Employee> onboard(@RequestBody @Valid OnboardEmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.onboardEmployee(request));
    }

    // FEATURE: Offboarding
    @PostMapping("/{id}/offboard")
    public ResponseEntity<Void> offboard(
            @PathVariable UUID id,
            @RequestBody @Valid OffboardRequest request) {
        employeeService.offboardEmployee(id, request);
        return ResponseEntity.ok().build();
    }

}
