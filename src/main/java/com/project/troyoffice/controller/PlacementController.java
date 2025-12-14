package com.project.troyoffice.controller;

import com.project.troyoffice.dto.DeployEmployeeRequest;
import com.project.troyoffice.model.Placement;
import com.project.troyoffice.service.PlacementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/placements")
@RequiredArgsConstructor
public class PlacementController {

    private final PlacementService placementService;

    // FEATURE: Active Placement List
    @GetMapping
    public ResponseEntity<Page<Placement>> getActivePlacements(Pageable pageable) {
        return ResponseEntity.ok(placementService.getActivePlacements(pageable));
    }

    // FEATURE: Deploy Employee
    @PostMapping("/deploy")
    public ResponseEntity<Placement> deploy(@RequestBody @Valid DeployEmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(placementService.deployEmployee(request));
    }
}
