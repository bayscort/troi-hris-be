package com.project.troyoffice.mapper;

import com.project.troyoffice.dto.EmployeeRequestDTO;
import com.project.troyoffice.dto.EmployeeResponseDTO;
import com.project.troyoffice.dto.OnboardEmployeeRequest;
import com.project.troyoffice.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee toEntity(EmployeeRequestDTO dto);
    Employee toEntity(OnboardEmployeeRequest dto);

    EmployeeResponseDTO toDTO(Employee entity);

    @Mapping(target = "id", ignore = true)
    void toUpdate(@MappingTarget Employee entity, EmployeeRequestDTO dto);

}
