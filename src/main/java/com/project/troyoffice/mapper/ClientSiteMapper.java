package com.project.troyoffice.mapper;

import com.project.troyoffice.dto.ClientSiteDTO;
import com.project.troyoffice.model.ClientSite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClientSiteMapper {

    @Mapping(target = "client.id", source = "clientId")
    ClientSite toEntity(ClientSiteDTO dto);

    @Mapping(target = "clientId", source = "client.id")
    ClientSiteDTO toDTO(ClientSite entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client.id", source = "clientId")
    void toUpdate(@MappingTarget ClientSite entity, ClientSiteDTO dto);
}

