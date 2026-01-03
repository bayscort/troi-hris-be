package com.project.troyoffice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BulkPatternItemsRequest {
    @NotNull
    private UUID patternId;
    @NotNull
    private List<AddPatternItemRequest> items;
}
