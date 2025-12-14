package com.project.troyoffice.service;

import com.project.troyoffice.dto.*;
import com.project.troyoffice.mapper.RoleMapper;
import com.project.troyoffice.model.Permission;
import com.project.troyoffice.model.Role;
import com.project.troyoffice.model.RoleMenuPermission;
import com.project.troyoffice.repository.RoleMenuPermissionRepository;
import com.project.troyoffice.repository.RoleRepository;
import com.project.troyoffice.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    private final RoleMenuPermissionRepository roleMenuPermissionRepository;

    @Transactional(readOnly = true)
    public List<RoleResponseDTO> findAll() {
        final List<Role> roleList = roleRepository.findByActiveTrue(Sort.by("id"));
        return roleList.stream()
                .map(roleMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoleResponseDTO get(UUID id) {
        return roleRepository.findById(id)
                .map(roleMapper::toDTO)
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(RoleRequestDTO dto) {
        Role entity = roleMapper.toEntity(dto);
        entity.setActive(true);
        return roleRepository.save(entity).getId();
    }

    public void update(UUID id, RoleRequestDTO dto) {
        Role entity = roleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        roleMapper.toUpdate(entity, dto);
        roleRepository.save(entity);
    }

    public void delete(UUID id) {
        Role entity = roleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        entity.setActive(false);
        roleRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<RoleDetailResponseDTO> getMenuPermission() {
        List<Role> roles = roleRepository.findAll();
        List<RoleMenuPermission> allMappings = roleMenuPermissionRepository.findAll();

        Map<UUID, RoleDetailResponseDTO> roleMap = new HashMap<>();

        for (Role role : roles) {
            RoleDetailResponseDTO dto = new RoleDetailResponseDTO();
            dto.setRole(new RoleResponseDTO(role.getId(), role.getName()));
            dto.setMenuList(new ArrayList<>());
            roleMap.put(role.getId(), dto);
        }

        Map<UUID, Map<UUID, MenuDetailDTO>> menuMapByRole = new HashMap<>();

        for (RoleMenuPermission rmp : allMappings) {
            UUID roleId = rmp.getRole().getId();
            UUID menuId = rmp.getMenu().getId();
            String menuName = rmp.getMenu().getName();

            RoleDetailResponseDTO roleDTO = roleMap.get(roleId);
            if (roleDTO == null) continue;

            menuMapByRole.putIfAbsent(roleId, new HashMap<>());
            Map<UUID, MenuDetailDTO> menuMap = menuMapByRole.get(roleId);

            MenuDetailDTO menuDTO = menuMap.get(menuId);
            if (menuDTO == null) {
                menuDTO = new MenuDetailDTO();
                menuDTO.setId(menuId);
                menuDTO.setName(menuName);
                menuDTO.setPermissionList(new ArrayList<>());
                menuMap.put(menuId, menuDTO);
                roleDTO.getMenuList().add(menuDTO);
            }

            Permission perm = rmp.getPermission();
            PermissionResponseDTO permDTO = new PermissionResponseDTO();
            permDTO.setId(perm.getId());
            permDTO.setOperation(perm.getOperation());

            menuDTO.getPermissionList().add(permDTO);
        }

        return new ArrayList<>(roleMap.values());
    }

}
