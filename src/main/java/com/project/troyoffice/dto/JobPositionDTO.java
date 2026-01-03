package com.project.troyoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class JobPositionDTO {

    private UUID id;

    private String title;

    private String code;

    private String level;

    private String internalGradeCode;

    private BigDecimal billingRateMin;

    private BigDecimal billingRateMax;

    private String description;

    private boolean isActive = true;
}
