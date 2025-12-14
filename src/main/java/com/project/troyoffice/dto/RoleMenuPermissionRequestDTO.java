package com.project.troyoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RoleMenuPermissionRequestDTO {

    private UUID roleId;

    private UUID menuId;

    private UUID permissionId;

}
