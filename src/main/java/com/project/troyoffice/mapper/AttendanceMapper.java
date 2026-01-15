package com.project.troyoffice.mapper;

import com.project.troyoffice.dto.AttendanceDetailResponse;
import com.project.troyoffice.dto.AttendanceRequest;
import com.project.troyoffice.dto.AttendanceResponse;
import com.project.troyoffice.model.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    Attendance toEntity(AttendanceRequest dto);

    AttendanceResponse toDTO(Attendance entity);
    AttendanceDetailResponse toDetailDTO(Attendance entity);


}
