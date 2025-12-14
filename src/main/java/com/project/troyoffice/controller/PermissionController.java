package com.project.troyoffice.controller;

import com.project.troyoffice.dto.PermissionRequestDTO;
import com.project.troyoffice.dto.PermissionResponseDTO;
import com.project.troyoffice.service.PermissionService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<PermissionResponseDTO>> getAll() {
        return ResponseEntity.ok(permissionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> getById(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(permissionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> create(@RequestBody PermissionRequestDTO dto) {
        final UUID createdId = permissionService.create(dto);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> update(@PathVariable(name = "id") UUID id,
                                       @RequestBody PermissionRequestDTO dto) {
        permissionService.update(id, dto);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) {
        permissionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
