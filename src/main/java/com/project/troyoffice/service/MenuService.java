package com.project.troyoffice.service;

import com.project.troyoffice.dto.MenuRequestDTO;
import com.project.troyoffice.dto.MenuResponseDTO;
import com.project.troyoffice.mapper.MenuMapper;
import com.project.troyoffice.model.Menu;
import com.project.troyoffice.repository.MenuRepository;
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
public class MenuService {

    private final MenuRepository menuRepository;

    private final MenuMapper menuMapper;

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findAll() {
        final List<Menu> menuList = menuRepository.findAll(Sort.by("id"));
        return menuList.stream()
                .map(menuMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public MenuResponseDTO get(UUID id) {
        return menuRepository.findById(id)
                .map(menuMapper::toDTO)
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(MenuRequestDTO dto) {
        Menu entity = menuMapper.toEntity(dto);
        return menuRepository.save(entity).getId();
    }

    public void update(UUID id, MenuRequestDTO dto) {
        Menu entity = menuRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        menuMapper.toUpdate(entity, dto);
        menuRepository.save(entity);
    }

    public void delete(UUID id) {
        menuRepository.deleteById(id);
    }

}
