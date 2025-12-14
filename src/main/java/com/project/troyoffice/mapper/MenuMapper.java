package com.project.troyoffice.mapper;

import com.project.troyoffice.dto.MenuRequestDTO;
import com.project.troyoffice.dto.MenuResponseDTO;
import com.project.troyoffice.model.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MenuMapper {

    Menu toEntity(MenuRequestDTO dto);

    MenuResponseDTO toDTO(Menu entity);

    @Mapping(target = "id", ignore = true)
    void toUpdate(@MappingTarget Menu entity, MenuRequestDTO dto);

}
