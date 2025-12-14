package com.project.troyoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class RoleMenuPermissionUpdateRequestDTO {

    private UUID roleId;

    private List<MenuPermissionUpdateRequestDTO> menuPermissions;

}
