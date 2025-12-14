package com.project.troyoffice.service;

import com.project.troyoffice.dto.ClientSiteDTO;
import com.project.troyoffice.mapper.ClientSiteMapper;
import com.project.troyoffice.model.ClientSite;
import com.project.troyoffice.repository.ClientRepository;
import com.project.troyoffice.repository.ClientSiteRepository;
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
public class ClientSiteService {

    private final ClientSiteRepository clientSiteRepository;
    private final ClientRepository clientRepository;
    private final ClientSiteMapper clientSiteMapper;

    @Transactional(readOnly = true)
    public List<ClientSiteDTO> findAll() {
        final List<ClientSite> list =
                clientSiteRepository.findByActiveTrue(Sort.by("createdAt"));
        return list.stream()
                .map(clientSiteMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ClientSiteDTO> findByClient(UUID clientId) {
        final List<ClientSite> list =
                clientSiteRepository.findByClientIdAndActiveTrue(clientId, Sort.by("createdAt"));
        return list.stream()
                .map(clientSiteMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClientSiteDTO get(UUID id) {
        return clientSiteRepository.findById(id)
                .map(clientSiteMapper::toDTO)
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(ClientSiteDTO dto) {
        // pastikan client exist
        clientRepository.findById(dto.getClientId())
                .orElseThrow(NotFoundException::new);

        ClientSite entity = clientSiteMapper.toEntity(dto);
        entity.setActive(true);
        return clientSiteRepository.save(entity).getId();
    }

    public void update(UUID id, ClientSiteDTO dto) {
        ClientSite entity = clientSiteRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        // cek client baru (jika berubah)
        if (dto.getClientId() != null) {
            clientRepository.findById(dto.getClientId())
                    .orElseThrow(NotFoundException::new);
        }

        clientSiteMapper.toUpdate(entity, dto);
        clientSiteRepository.save(entity);
    }

    public void delete(UUID id) {
        ClientSite entity = clientSiteRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        entity.setActive(false);
        clientSiteRepository.save(entity);
    }
}

