package com.project.troyoffice.controller;

import com.project.troyoffice.dto.RoleMenuPermissionRequestDTO;
import com.project.troyoffice.dto.RoleMenuPermissionResponseDTO;
import com.project.troyoffice.dto.RoleMenuPermissionUpdateRequestDTO;
import com.project.troyoffice.service.RoleMenuPermissionService;
import com.project.troyoffice.service.RoleService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/role-menu-permissions", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RoleMenuPermissionController {

    private final RoleMenuPermissionService roleMenuPermissionService;
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleMenuPermissionResponseDTO>> getAll() {
        return ResponseEntity.ok(roleMenuPermissionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleMenuPermissionResponseDTO> getById(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(roleMenuPermissionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> create(@RequestBody RoleMenuPermissionRequestDTO dto) {
        final UUID createdId = roleMenuPermissionService.create(dto);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) {
        roleMenuPermissionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<Void> updateRoleMenuPermissions(
            @PathVariable UUID roleId,
            @RequestBody RoleMenuPermissionUpdateRequestDTO request) {
        roleMenuPermissionService.updateRoleMenuPermissions(roleId, request);
        return ResponseEntity.noContent().build();
    }

}
