package com.project.troyoffice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ClientSiteDTO {

    private UUID id;

    private UUID clientId;

    private String name;

    private String address;

    private Double latitude;

    private Double longitude;

    private Integer radiusMeters;

    private Boolean active;
}

