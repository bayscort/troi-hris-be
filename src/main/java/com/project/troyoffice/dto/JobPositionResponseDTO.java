package com.project.troyoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class JobPositionResponseDTO {

    private UUID id;

    private ClientDTO client;

    private String title;

    private String code;

    private String level;

    private String internalGradeCode;

    private BigDecimal billingRateMin;

    private BigDecimal billingRateMax;

    private String description;

    private boolean isActive = true;

}
