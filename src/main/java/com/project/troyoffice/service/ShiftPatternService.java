package com.project.troyoffice.service;

import com.project.troyoffice.dto.AddPatternItemRequest;
import com.project.troyoffice.dto.BulkPatternItemsRequest;
import com.project.troyoffice.dto.CreatePatternRequest;
import com.project.troyoffice.model.ShiftMaster;
import com.project.troyoffice.model.ShiftPattern;
import com.project.troyoffice.model.ShiftPatternItem;
import com.project.troyoffice.repository.ShiftMasterRepository;
import com.project.troyoffice.repository.ShiftPatternItemRepository;
import com.project.troyoffice.repository.ShiftPatternRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ShiftPatternService {

    private final ShiftMasterRepository shiftMasterRepository;
    private final ShiftPatternRepository shiftPatternRepository;
    private final ShiftPatternItemRepository shiftPatternItemRepository;

    public UUID create(CreatePatternRequest req) {
        ShiftPattern pattern = new ShiftPattern();
        pattern.setClientId(req.getClientId());
        pattern.setName(req.getName());
        pattern.setCycleDays(req.getCycleDays());
        return shiftPatternRepository.save(pattern).getId();
    }

    public void addPatternItems(BulkPatternItemsRequest req) {
        ShiftPattern pattern = shiftPatternRepository.findById(req.getPatternId())
                .orElseThrow(() -> new RuntimeException("Pattern not found"));

        for (AddPatternItemRequest itemReq : req.getItems()) {
            ShiftMaster master = shiftMasterRepository.findById(itemReq.getShiftMasterId())
                    .orElseThrow(() -> new RuntimeException("Shift Master not found"));

            ShiftPatternItem item = new ShiftPatternItem();
            item.setShiftPattern(pattern);
            item.setDaySequence(itemReq.getDaySequence());
            item.setShiftMaster(master);

            shiftPatternItemRepository.save(item);
        }
    }

}
