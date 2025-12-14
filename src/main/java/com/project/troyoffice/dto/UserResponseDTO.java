package com.project.troyoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserResponseDTO {

    private UUID id;

    private String name;
    private String username;
    private RoleResponseDTO role;

}
