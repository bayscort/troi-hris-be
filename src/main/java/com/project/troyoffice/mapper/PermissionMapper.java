package com.project.troyoffice.mapper;

import com.project.troyoffice.dto.PermissionRequestDTO;
import com.project.troyoffice.dto.PermissionResponseDTO;
import com.project.troyoffice.model.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toEntity(PermissionRequestDTO dto);

    PermissionResponseDTO toDTO(Permission entity);

    @Mapping(target = "id", ignore = true)
    void toUpdate(@MappingTarget Permission entity, PermissionRequestDTO dto);

}
