package com.project.troyoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MenuDetailDTO {

    private UUID id;
    private String name;
    private List<PermissionResponseDTO> permissionList;

}
