package com.project.troyoffice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OffboardRequest {
    @NotNull
    private LocalDate effectiveDate;

    @NotBlank
    private String reason;
}
