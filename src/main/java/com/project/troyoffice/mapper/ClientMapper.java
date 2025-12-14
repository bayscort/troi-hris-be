package com.project.troyoffice.mapper;

import com.project.troyoffice.dto.ClientDTO;
import com.project.troyoffice.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(ClientDTO dto);

    ClientDTO toDTO(Client entity);

    @Mapping(target = "id", ignore = true)
    void toUpdate(@MappingTarget Client entity, ClientDTO dto);

}
