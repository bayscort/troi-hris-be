package com.project.troyoffice.service;

import com.project.troyoffice.dto.ClientDTO;
import com.project.troyoffice.mapper.ClientMapper;
import com.project.troyoffice.model.Client;
import com.project.troyoffice.repository.ClientRepository;
import com.project.troyoffice.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    @Transactional(readOnly = true)
    public List<ClientDTO> findAll() {
        final List<Client> clientList = clientRepository.findByActiveTrue(Sort.by("createdAt"));
        return clientList.stream()
                .map(clientMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClientDTO get(UUID id) {
        return clientRepository.findById(id)
                .map(clientMapper::toDTO)
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(ClientDTO dto) {
        Client entity = clientMapper.toEntity(dto);
        entity.setActive(true);
        return clientRepository.save(entity).getId();
    }

    public void update(UUID id, ClientDTO dto) {
        Client entity = clientRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        clientMapper.toUpdate(entity, dto);
        clientRepository.save(entity);
    }

    public void delete(UUID id) {
        Client entity = clientRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        entity.setActive(false);
        clientRepository.save(entity);
    }

}
