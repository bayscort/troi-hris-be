package com.project.troyoffice.controller;

import com.project.troyoffice.dto.BulkPatternItemsRequest;
import com.project.troyoffice.dto.CreatePatternRequest;
import com.project.troyoffice.service.ShiftPatternService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/shift-pattern", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ShitPatternController {

    private final ShiftPatternService shiftPatternService;

    @PostMapping
    public ResponseEntity<?> createPattern(@RequestBody CreatePatternRequest req) {
        var result = shiftPatternService.create(req);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/items")
    public ResponseEntity<?> addPatternItems(@RequestBody BulkPatternItemsRequest req) {
        shiftPatternService.addPatternItems(req);
        return ResponseEntity.ok("Items added successfully");
    }

    @GetMapping
    public ResponseEntity<?> getPatterns(@RequestParam UUID clientId) {
        var result = shiftPatternService.getAllByClient(clientId);
        return ResponseEntity.ok(result);
    }

}
