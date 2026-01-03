package com.project.troyoffice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CreateJobPositionRequest {

    @NotNull(message = "Client ID tidak boleh kosong")
    private UUID clientId;

    @NotBlank(message = "Nama Jabatan (Title) tidak boleh kosong")
    @Size(max = 100, message = "Nama jabatan maksimal 100 karakter")
    private String title;

    @NotBlank(message = "Kode Jabatan tidak boleh kosong")
    @Size(max = 50, message = "Kode jabatan maksimal 50 karakter")
    // Tips: Biasanya kode ini uppercase, bisa dinormalisasi di Service
    private String code;

    @Size(max = 50)
    private String level; // e.g. "Junior", "Staff", "Manager"

    // Mapping ke Grade Internal perusahaan Outsource
    @Size(max = 50)
    private String internalGradeCode;

    // Rate Card (Opsional)
    @Min(value = 0, message = "Rate minimum tidak boleh negatif")
    private BigDecimal billingRateMin;

    @Min(value = 0, message = "Rate maximum tidak boleh negatif")
    private BigDecimal billingRateMax;

    private String description;

}
