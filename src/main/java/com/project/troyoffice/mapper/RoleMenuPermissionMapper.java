package com.project.troyoffice.mapper;

import com.project.troyoffice.dto.RoleMenuPermissionRequestDTO;
import com.project.troyoffice.dto.RoleMenuPermissionResponseDTO;
import com.project.troyoffice.model.RoleMenuPermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMenuPermissionMapper {

    RoleMenuPermission toEntity(RoleMenuPermissionRequestDTO dto);

    RoleMenuPermissionResponseDTO toDTO(RoleMenuPermission entity);

    @Mapping(target = "id", ignore = true)
    RoleMenuPermission toUpdate(@MappingTarget RoleMenuPermission entity, RoleMenuPermissionRequestDTO dto);

}
