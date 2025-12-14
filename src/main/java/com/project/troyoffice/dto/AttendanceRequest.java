package com.project.troyoffice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AttendanceRequest {

    private Double latitude;
    private Double longitude;
    private MultipartFile file;

}
