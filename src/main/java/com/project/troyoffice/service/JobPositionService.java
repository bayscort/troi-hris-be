package com.project.troyoffice.service;

import com.project.troyoffice.dto.ClientSiteDTO;
import com.project.troyoffice.dto.CreateJobPositionRequest;
import com.project.troyoffice.dto.JobPositionResponseDTO;
import com.project.troyoffice.mapper.JobPositionMapper;
import com.project.troyoffice.model.Client;
import com.project.troyoffice.model.ClientSite;
import com.project.troyoffice.model.JobPosition;
import com.project.troyoffice.repository.ClientRepository;
import com.project.troyoffice.repository.JobPositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class JobPositionService {

    private final JobPositionRepository jobPositionRepo;
    private final ClientRepository clientRepo;

    private final JobPositionMapper jobPositionMapper;

    public JobPosition create(CreateJobPositionRequest req) {
        Client clientProxy = clientRepo.getReferenceById(req.getClientId());

        JobPosition jobPosition = JobPosition.builder()
                .client(clientProxy)
                .title(req.getTitle())
                .code(req.getCode())
                .level(req.getLevel())
                .internalGradeCode(req.getInternalGradeCode())
                .build();
        return jobPositionRepo.save(jobPosition);
    }

    @Transactional(readOnly = true)
    public List<JobPositionResponseDTO> findAll() {
        final List<JobPosition> jobPositions = jobPositionRepo.findAll(Sort.by("id"));
        return jobPositions.stream()
                .map(jobPositionMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<JobPositionResponseDTO> findByClient(UUID clientId) {
        final List<JobPosition> list =
                jobPositionRepo.findByClientIdAndIsActiveTrue(clientId, Sort.by("createdAt"));
        return list.stream()
                .map(jobPositionMapper::toDTO)
                .toList();
    }

}
