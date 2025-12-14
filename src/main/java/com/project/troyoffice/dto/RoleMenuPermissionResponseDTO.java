package com.project.troyoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RoleMenuPermissionResponseDTO {

    private UUID id;

    private RoleResponseDTO role;

    private MenuResponseDTO menu;

    private PermissionResponseDTO permission;

}
