package com.project.troyoffice.controller;

import com.project.troyoffice.dto.MenuRequestDTO;
import com.project.troyoffice.dto.MenuResponseDTO;
import com.project.troyoffice.service.MenuService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/menus", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<List<MenuResponseDTO>> getAll() {
        return ResponseEntity.ok(menuService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuResponseDTO> getById(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(menuService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> create(@RequestBody MenuRequestDTO dto) {
        final UUID createdId = menuService.create(dto);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> update(@PathVariable(name = "id") UUID id,
                                       @RequestBody MenuRequestDTO dto) {
        menuService.update(id, dto);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) {
        menuService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
