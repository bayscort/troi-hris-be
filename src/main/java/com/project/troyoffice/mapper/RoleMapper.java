package com.project.troyoffice.mapper;

import com.project.troyoffice.dto.RoleRequestDTO;
import com.project.troyoffice.dto.RoleResponseDTO;
import com.project.troyoffice.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toEntity(RoleRequestDTO dto);

    RoleResponseDTO toDTO(Role entity);

    @Mapping(target = "id", ignore = true)
    void toUpdate(@MappingTarget Role entity, RoleRequestDTO dto);

}
