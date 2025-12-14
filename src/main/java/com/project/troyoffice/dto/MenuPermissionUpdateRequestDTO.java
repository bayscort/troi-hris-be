package com.project.troyoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MenuPermissionUpdateRequestDTO {

    private UUID menuId;

    private List<UUID> permissionIds;

}
