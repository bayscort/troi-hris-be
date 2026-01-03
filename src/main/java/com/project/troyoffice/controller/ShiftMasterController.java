package com.project.troyoffice.controller;

import com.project.troyoffice.dto.CreateShiftMasterRequest;
import com.project.troyoffice.service.ShiftMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
