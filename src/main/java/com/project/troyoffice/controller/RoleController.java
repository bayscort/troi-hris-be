package com.project.troyoffice.controller;

import com.project.troyoffice.dto.RoleDetailResponseDTO;
import com.project.troyoffice.dto.RoleRequestDTO;
import com.project.troyoffice.dto.RoleResponseDTO;
import com.project.troyoffice.service.RoleService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getById(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(roleService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> create(@RequestBody RoleRequestDTO dto) {
        final UUID createdId = roleService.create(dto);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> update(@PathVariable(name = "id") UUID id,
                                       @RequestBody RoleRequestDTO dto) {
        roleService.update(id, dto);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/menu-permission")
    public ResponseEntity<List<RoleDetailResponseDTO>> getMenuPermission() {
        return ResponseEntity.ok(roleService.getMenuPermission());
    }

}
