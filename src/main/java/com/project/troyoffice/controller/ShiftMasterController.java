package com.project.troyoffice.controller;

import com.project.troyoffice.dto.CreateShiftMasterRequest;
import com.project.troyoffice.dto.ShiftMasterDTO;
import com.project.troyoffice.service.ShiftMasterService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/shift-masters", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ShiftMasterController {

    private final ShiftMasterService shiftMasterService;

    @PostMapping
    public ResponseEntity<?> createShift(@RequestBody CreateShiftMasterRequest req) {
        var result = shiftMasterService.create(req);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<ShiftMasterDTO>> getAll(@RequestParam(required = false) UUID clientId) {
        return ResponseEntity.ok(shiftMasterService.findAll(clientId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftMasterDTO> getById(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(shiftMasterService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> update(@PathVariable(name = "id") UUID id,
                                       @RequestBody CreateShiftMasterRequest dto) {
        shiftMasterService.update(id, dto);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) {
        shiftMasterService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
