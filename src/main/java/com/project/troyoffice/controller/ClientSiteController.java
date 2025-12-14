package com.project.troyoffice.controller;

import com.project.troyoffice.dto.ClientSiteDTO;
import com.project.troyoffice.service.ClientSiteService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/client-sites", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ClientSiteController {

    private final ClientSiteService clientSiteService;

    @GetMapping
    public ResponseEntity<List<ClientSiteDTO>> getAll() {
        return ResponseEntity.ok(clientSiteService.findAll());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ClientSiteDTO>> getByClient(@PathVariable UUID clientId) {
        return ResponseEntity.ok(clientSiteService.findByClient(clientId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientSiteDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(clientSiteService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> create(@RequestBody ClientSiteDTO dto) {
        UUID createdId = clientSiteService.create(dto);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> update(@PathVariable UUID id, @RequestBody ClientSiteDTO dto) {
        clientSiteService.update(id, dto);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        clientSiteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

