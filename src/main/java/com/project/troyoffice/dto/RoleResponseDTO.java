package com.project.troyoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class RoleResponseDTO {

    private UUID id;

    private String name;

}
