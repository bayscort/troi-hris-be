package com.project.troyoffice.service;

import com.project.troyoffice.dto.CreateShiftMasterRequest;
import com.project.troyoffice.model.ShiftMaster;
import com.project.troyoffice.repository.ShiftMasterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ShiftMasterService {

    private final ShiftMasterRepository shiftMasterRepository;

    @Transactional
    public UUID create(CreateShiftMasterRequest req) {
        ShiftMaster shift = ShiftMaster.builder()
                .clientId(req.getClientId())
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

}
