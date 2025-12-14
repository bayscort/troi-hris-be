package com.project.troyoffice.mapper;

import com.project.troyoffice.dto.AttendanceDetailResponse;
import com.project.troyoffice.dto.AttendanceRequest;
import com.project.troyoffice.dto.AttendanceResponse;
import com.project.troyoffice.model.Attendance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    Attendance toEntity(AttendanceRequest dto);

    AttendanceResponse toDTO(Attendance entity);
    AttendanceDetailResponse toDetailDTO(Attendance entity);


}
