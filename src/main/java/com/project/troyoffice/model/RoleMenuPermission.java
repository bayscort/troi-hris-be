package com.project.troyoffice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class RoleMenuPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Role role;

    @ManyToOne
    private Menu menu;

    @ManyToOne
    private Permission permission;

}
