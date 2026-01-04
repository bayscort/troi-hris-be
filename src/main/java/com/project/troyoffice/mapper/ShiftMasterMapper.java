package com.project.troyoffice.mapper;

import com.project.troyoffice.dto.CreateShiftMasterRequest;
import com.project.troyoffice.dto.ShiftMasterDTO;
import com.project.troyoffice.model.ShiftMaster;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ShiftMasterMapper {

    ShiftMaster toEntity(CreateShiftMasterRequest dto);

    ShiftMasterDTO toDTO(ShiftMaster entity);

    @Mapping(target = "id", ignore = true)
    void toUpdate(@MappingTarget ShiftMaster entity, CreateShiftMasterRequest dto);

}
