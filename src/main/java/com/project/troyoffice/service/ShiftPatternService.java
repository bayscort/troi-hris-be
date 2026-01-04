package com.project.troyoffice.service;

import com.project.troyoffice.dto.*;
import com.project.troyoffice.model.Client;
import com.project.troyoffice.model.ShiftMaster;
import com.project.troyoffice.model.ShiftPattern;
import com.project.troyoffice.model.ShiftPatternItem;
import com.project.troyoffice.repository.ClientRepository;
import com.project.troyoffice.repository.ShiftMasterRepository;
import com.project.troyoffice.repository.ShiftPatternItemRepository;
import com.project.troyoffice.repository.ShiftPatternRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ShiftPatternService {

    private final ShiftMasterRepository shiftMasterRepository;
    private final ShiftPatternRepository shiftPatternRepository;
    private final ShiftPatternItemRepository shiftPatternItemRepository;
    private final ClientRepository clientRepository;

    public UUID create(CreatePatternRequest req) {
        ShiftPattern pattern = new ShiftPattern();

        Client clientProxy = clientRepository.getReferenceById(req.getClientId());
        pattern.setClient(clientProxy);
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

    @Transactional(readOnly = true)
    public List<ShiftPatternDTO.Response> getAllByClient(UUID clientId) {
        List<ShiftPattern> patterns = shiftPatternRepository.findAllByClientId(clientId);

        return patterns.stream().map(p -> {
            // Map Items
            List<ShiftPatternDTO.PatternItemResponse> items = p.getItems().stream()
                    .sorted(Comparator.comparingInt(ShiftPatternItem::getDaySequence)) // Pastikan urut
                    .map(i -> ShiftPatternDTO.PatternItemResponse.builder()
                            .daySequence(i.getDaySequence())
                            .shiftMasterId(i.getShiftMaster().getId())
                            .shiftName(i.getShiftMaster().getName())
                            .shiftCode(i.getShiftMaster().getCode())
                            .shiftColor(i.getShiftMaster().getColor()) // Asumsi ada field color
                            .startTime(i.getShiftMaster().getStartTime().toString())
                            .endTime(i.getShiftMaster().getEndTime().toString())
                            .build())
                    .collect(Collectors.toList());

            // Map Header
            return ShiftPatternDTO.Response.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .cycleDays(p.getCycleDays())
                    .client(ClientDTO.builder()
                            .id(p.getClient().getId())
                            .name(p.getClient().getName())
                            .build())
                    .items(items)
                    .build();
        }).collect(Collectors.toList());
    }

}
