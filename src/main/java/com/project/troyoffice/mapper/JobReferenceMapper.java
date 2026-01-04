package com.project.troyoffice.mapper;

import com.project.troyoffice.dto.JobReferenceDTO;
import com.project.troyoffice.model.JobReference;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobReferenceMapper {

    JobReferenceDTO toDTO(JobReference entity);

}
