package com.project.troyoffice.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.UUID;

public class ShiftPatternDTO {

    @Data
    @Builder
    public static class Response {
        private UUID id;
        private String name;
        private Integer cycleDays;
        private ClientDTO client; // Ringkasan client
        private List<PatternItemResponse> items; // Detail urutan
    }

    @Data
    @Builder
    public static class PatternItemResponse {
        private Integer daySequence;
        private UUID shiftMasterId;
        private String shiftName;
        private String shiftCode;
        private String shiftColor; // Penting untuk UI!
        private String startTime;
        private String endTime;
    }
}
