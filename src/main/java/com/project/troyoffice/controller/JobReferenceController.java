package com.project.troyoffice.controller;

import com.project.troyoffice.dto.JobReferenceDTO;
import com.project.troyoffice.service.JobReferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/job-references", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class JobReferenceController {

    private final JobReferenceService jobReferenceService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody JobReferenceDTO req) {
        var result = jobReferenceService.create(req);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<JobReferenceDTO>> getAll() {
        return ResponseEntity.ok(jobReferenceService.findAll());
    }

}
