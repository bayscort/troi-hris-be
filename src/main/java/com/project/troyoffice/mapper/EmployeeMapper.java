package com.project.troyoffice.mapper;

import com.project.troyoffice.dto.EmployeeRequestDTO;
import com.project.troyoffice.dto.EmployeeResponseDTO;
import com.project.troyoffice.dto.OnboardEmployeeRequest;
import com.project.troyoffice.model.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "educations", ignore = true)
    @Mapping(target = "jobReferences", ignore = true)
    Employee toEntity(EmployeeRequestDTO dto);

    Employee toEntity(OnboardEmployeeRequest dto);

    EmployeeResponseDTO toDTO(Employee entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "educations", ignore = true)
    @Mapping(target = "jobReferences", ignore = true)
    void updateEntity(EmployeeRequestDTO dto, @MappingTarget Employee entity);

}
