package com.project.troyoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MenuPermissionDTO {
    private String name;
    private List<String> permissions;
}
