package com.project.troyoffice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreatePatternRequest {
    @NotNull
    private UUID clientId;
    @NotNull
    private String name;
    @NotNull
    private Integer cycleDays; // Misal: 3 hari
}
