package com.project.troyoffice.service;

import com.project.troyoffice.dto.MenuPermissionUpdateRequestDTO;
import com.project.troyoffice.dto.RoleMenuPermissionRequestDTO;
import com.project.troyoffice.dto.RoleMenuPermissionResponseDTO;
import com.project.troyoffice.dto.RoleMenuPermissionUpdateRequestDTO;
import com.project.troyoffice.mapper.RoleMenuPermissionMapper;
import com.project.troyoffice.model.Menu;
import com.project.troyoffice.model.Permission;
import com.project.troyoffice.model.Role;
import com.project.troyoffice.model.RoleMenuPermission;
import com.project.troyoffice.repository.MenuRepository;
import com.project.troyoffice.repository.PermissionRepository;
import com.project.troyoffice.repository.RoleMenuPermissionRepository;
import com.project.troyoffice.repository.RoleRepository;
import com.project.troyoffice.util.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoleMenuPermissionService {

    private final RoleMenuPermissionRepository roleMenuPermissionRepository;
    private final RoleRepository roleRepository;

    private final RoleMenuPermissionMapper roleMenuPermissionMapper;
    private final MenuRepository menuRepository;
    private final PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    public List<RoleMenuPermissionResponseDTO> findAll() {
        final List<RoleMenuPermission> roleMenuPermissionList = roleMenuPermissionRepository.findAll(Sort.by("id"));
        return roleMenuPermissionList.stream()
                .map(roleMenuPermissionMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoleMenuPermissionResponseDTO get(UUID id) {
        return roleMenuPermissionRepository.findById(id)
                .map(roleMenuPermissionMapper::toDTO)
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(RoleMenuPermissionRequestDTO dto) {
        RoleMenuPermission entity = roleMenuPermissionMapper.toEntity(dto);

        Role role = roleRepository.findById(dto.getRoleId()).orElseThrow(() -> new RuntimeException("Role not found"));
        entity.setRole(role);

        Menu menu = menuRepository.findById(dto.getRoleId()).orElseThrow(() -> new RuntimeException("Menu not found"));
        entity.setMenu(menu);

        Permission permission = permissionRepository.findById(dto.getRoleId()).orElseThrow(() -> new RuntimeException("Permission not found"));
        entity.setPermission(permission);

        return roleMenuPermissionRepository.save(entity).getId();
    }

    public void update(UUID id, RoleMenuPermissionRequestDTO dto) {
        RoleMenuPermission entity = roleMenuPermissionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        RoleMenuPermission updatedEntity = roleMenuPermissionMapper.toUpdate(entity, dto);
        Role role = roleRepository.findById(dto.getRoleId()).orElseThrow(() -> new RuntimeException("Role not found"));
        updatedEntity.setRole(role);

        Menu menu = menuRepository.findById(dto.getRoleId()).orElseThrow(() -> new RuntimeException("Menu not found"));
        updatedEntity.setMenu(menu);

        Permission permission = permissionRepository.findById(dto.getRoleId()).orElseThrow(() -> new RuntimeException("Permission not found"));
        updatedEntity.setPermission(permission);
        roleMenuPermissionRepository.save(entity);
    }

    public void delete(UUID id) {
        roleMenuPermissionRepository.deleteById(id);
    }

    public void updateRoleMenuPermissions(UUID roleId, RoleMenuPermissionUpdateRequestDTO request) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));

        roleMenuPermissionRepository.deleteByRoleId(roleId);

        List<RoleMenuPermission> newPermissions = new ArrayList<>();

        for (MenuPermissionUpdateRequestDTO menuPermission : request.getMenuPermissions()) {
            Menu menu = menuRepository.findById(menuPermission.getMenuId())
                    .orElseThrow(() -> new EntityNotFoundException("Menu not found with id: " + menuPermission.getMenuId()));

            for (UUID permissionId : menuPermission.getPermissionIds()) {
                Permission permission = permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + permissionId));

                RoleMenuPermission roleMenuPermission = new RoleMenuPermission();
                roleMenuPermission.setRole(role);
                roleMenuPermission.setMenu(menu);
                roleMenuPermission.setPermission(permission);
                newPermissions.add(roleMenuPermission);
            }
        }
        roleMenuPermissionRepository.saveAll(newPermissions);
    }

}
