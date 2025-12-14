package com.project.troyoffice.service;

import com.project.troyoffice.dto.PermissionRequestDTO;
import com.project.troyoffice.dto.PermissionResponseDTO;
import com.project.troyoffice.mapper.PermissionMapper;
import com.project.troyoffice.model.Permission;
import com.project.troyoffice.repository.PermissionRepository;
import com.project.troyoffice.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PermissionService {

    private final PermissionRepository permissionRepository;

    private final PermissionMapper permissionMapper;

    @Transactional(readOnly = true)
    public List<PermissionResponseDTO> findAll() {
        final List<Permission> permissionList = permissionRepository.findAll(Sort.by("id"));
        return permissionList.stream()
                .map(permissionMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PermissionResponseDTO get(UUID id) {
        return permissionRepository.findById(id)
                .map(permissionMapper::toDTO)
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(PermissionRequestDTO dto) {
        Permission entity = permissionMapper.toEntity(dto);
        return permissionRepository.save(entity).getId();
    }

    public void update(UUID id, PermissionRequestDTO dto) {
        Permission entity = permissionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        permissionMapper.toUpdate(entity, dto);
        permissionRepository.save(entity);
    }

    public void delete(UUID id) {
        permissionRepository.deleteById(id);
    }

}
