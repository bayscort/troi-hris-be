package com.project.troyoffice.service;

import com.project.troyoffice.dto.CreateShiftMasterRequest;
import com.project.troyoffice.dto.MenuRequestDTO;
import com.project.troyoffice.dto.MenuResponseDTO;
import com.project.troyoffice.dto.ShiftMasterDTO;
import com.project.troyoffice.mapper.ShiftMasterMapper;
import com.project.troyoffice.model.Client;
import com.project.troyoffice.model.JobReference;
import com.project.troyoffice.model.Menu;
import com.project.troyoffice.model.ShiftMaster;
import com.project.troyoffice.repository.ClientRepository;
import com.project.troyoffice.repository.ShiftMasterRepository;
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
public class ShiftMasterService {

    private final ShiftMasterRepository shiftMasterRepository;

    private final ClientRepository clientRepository;

    private final ShiftMasterMapper shiftMasterMapper;

    @Transactional
    public UUID create(CreateShiftMasterRequest req) {

        Client client =
                clientRepository.getReferenceById(req.getClientId());

        ShiftMaster shift = ShiftMaster.builder()
                .client(client)
                .code(req.getCode())
                .name(req.getName())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .isCrossDay(req.isCrossDay())
                .isDayOff(req.isDayOff())
                .lateToleranceMinutes(req.getLateToleranceMinutes())
                .clockInWindowMinutes(req.getClockInWindowMinutes())
                .build();
        return shiftMasterRepository.save(shift).getId();
    }

    @Transactional(readOnly = true)
    public List<ShiftMasterDTO> findAll(UUID clientId) {
        List<ShiftMaster> shiftMasterList;

        if (clientId == null) {
            shiftMasterList = shiftMasterRepository.findAll(Sort.by("id"));
        } else {
            shiftMasterList = shiftMasterRepository.findByClientId(clientId, Sort.by("id"));
        }

        return shiftMasterList.stream()
                .map(shiftMasterMapper::toDTO)
                .toList();
    }


    @Transactional(readOnly = true)
    public ShiftMasterDTO get(UUID id) {
        return shiftMasterRepository.findById(id)
                .map(shiftMasterMapper::toDTO)
                .orElseThrow(NotFoundException::new);
    }

    public void update(UUID id, CreateShiftMasterRequest dto) {
        ShiftMaster entity = shiftMasterRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        shiftMasterMapper.toUpdate(entity, dto);
        shiftMasterRepository.save(entity);
    }

    public void delete(UUID id) {
        shiftMasterRepository.deleteById(id);
    }

}
