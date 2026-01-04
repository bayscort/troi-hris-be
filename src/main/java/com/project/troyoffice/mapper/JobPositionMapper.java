package com.project.troyoffice.mapper;

import com.project.troyoffice.dto.JobPositionResponseDTO;
import com.project.troyoffice.model.JobPosition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobPositionMapper {

    JobPositionResponseDTO toDTO(JobPosition entity);

}
