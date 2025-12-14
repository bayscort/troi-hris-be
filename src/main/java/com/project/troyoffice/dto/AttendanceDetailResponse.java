package com.project.troyoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceDetailResponse extends AttendanceResponse {
    private String checkInPhotoBase64;
    private String checkOutPhotoBase64;
}
