package com.project.troyoffice.controller;

import com.project.troyoffice.dto.CreateJobPositionRequest;
import com.project.troyoffice.dto.JobPositionResponseDTO;
import com.project.troyoffice.service.JobPositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/job-positions", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class JobPositionController {

    private final JobPositionService jobPositionService;

    @PostMapping
    public ResponseEntity<?> createJobPosition(@RequestBody CreateJobPositionRequest req) {
        var result = jobPositionService.create(req);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<JobPositionResponseDTO>> getAll() {
        return ResponseEntity.ok(jobPositionService.findAll());
    }

}
