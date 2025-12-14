package com.project.troyoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ClientDTO {

    private UUID id;

    private String name;

    private String code;

    private String address;

    private String contactPerson;

    private String contactPhone;

    private Boolean isInternal;

    private Boolean active;

}
