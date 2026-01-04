package com.project.troyoffice.controller;

import com.project.troyoffice.dto.RosterDto;
import com.project.troyoffice.service.RosterService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/roster")
@RequiredArgsConstructor
public class RosterController {

    private final RosterService rosterService;

    // GET /api/v1/roster/matrix
    // Params:
    // - siteId (UUID)
    // - startDate (YYYY-MM-DD)
    // - endDate (YYYY-MM-DD)
    // - jobPositionId (UUID, Optional)

    @GetMapping("/matrix")
    public ResponseEntity<RosterDto.Response> getRosterMatrix(
            @RequestParam UUID siteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID jobPositionId
    ) {
        var result = rosterService.getRosterMatrix(siteId, jobPositionId, startDate, endDate);
        return ResponseEntity.ok(result);
    }
}
