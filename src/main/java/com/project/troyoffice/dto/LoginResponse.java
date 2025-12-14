package com.project.troyoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String name;
    private String username;
    private String role;
    private List<MenuPermissionDTO> menus;

}
