package com.project.troyoffice.service;

import com.project.troyoffice.dto.CreateJobPositionRequest;
import com.project.troyoffice.model.Client;
import com.project.troyoffice.model.JobPosition;
import com.project.troyoffice.repository.ClientRepository;
import com.project.troyoffice.repository.JobPositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class JobPositionService {

    private final JobPositionRepository jobPositionRepo;
    private final ClientRepository clientRepo;

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

}
