package com.project.troyoffice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AddPatternItemRequest {
    @NotNull
    private Integer daySequence; // Hari ke-1, 2, dst
    @NotNull
    private UUID shiftMasterId;
}
