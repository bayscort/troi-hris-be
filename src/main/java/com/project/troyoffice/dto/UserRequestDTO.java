package com.project.troyoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserRequestDTO {

    private String name;
    private String username;
    private String password;
    private UUID roleId;

}
